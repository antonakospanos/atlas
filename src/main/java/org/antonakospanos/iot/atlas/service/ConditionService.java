package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.dao.model.Condition;
import org.antonakospanos.iot.atlas.dao.model.ConditionStatement;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.dao.repository.ActionRepository;
import org.antonakospanos.iot.atlas.dao.repository.AlertRepository;
import org.antonakospanos.iot.atlas.dao.repository.ConditionRepository;
import org.antonakospanos.iot.atlas.dao.repository.ModuleRepository;
import org.apache.commons.validator.routines.DoubleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConditionService {

	@Autowired
	ModuleRepository moduleRepository;

	@Autowired
	ActionRepository actionRepository;

	@Autowired
	AlertRepository alertRepository;

	@Autowired
	ConditionRepository conditionRepository;


	public void linkModules(Condition condition) {

		if (condition != null && condition.getConditionOrStatements() != null) {

			condition.getConditionOrStatements()
					.stream()
					.forEach(conditionOrStatement -> {

						conditionOrStatement.getConditionAndStatements()
								.stream()
								.forEach(conditionAndStatement -> {
									ConditionStatement statement = conditionAndStatement.getConditionStatement();

									String conditionalDeviceId = statement.getDeviceExternalId();
									String conditionalModuleId = statement.getModuleExternalId();
									Module conditionalModule = moduleRepository.findByExternalId_AndDevice_ExternalId(conditionalModuleId, conditionalDeviceId);

									if (conditionalModule != null) {
										statement.setModule(conditionalModule);
									} else {
										throw new IllegalArgumentException("Module '" + conditionalModuleId + "' of device '" + conditionalDeviceId + "' does not exist!");
									}
								});
					});

		}
	}

	@Transactional
	public List<Condition> findAllValid() {
		List<Condition> conditions = conditionRepository.findAll();

		return findValid(conditions);
	}

	@Transactional
	public List<Condition> findValidWithAlerts() {
		List<Condition> conditions = conditionRepository.findByAlert_IdNotNull();

		return findValid(conditions);
	}

	@Transactional
	public List<Condition> findValid(List<Condition> conditions) {
		List<Condition> valid = new ArrayList<>();

		for (Condition condition : conditions) {
			if (isValid(condition)) {
				valid.add(condition);
			}
		}

		return valid;
	}

	@Transactional
	public boolean isValid(Condition condition) {
		boolean valid = false;

		if (condition == null || condition.getConditionOrStatements() == null) {
			return true;
		}

		// Predicate construction
		List<List<Boolean>> orLegs = new ArrayList<>();

		condition.getConditionOrStatements()
				.stream()
				.forEach(conditionOrStatement -> {

					List<Boolean> andLegs = new ArrayList<>();

					conditionOrStatement.getConditionAndStatements()
							.stream()
							.forEach(conditionAndStatement -> {
								ConditionStatement statement = conditionAndStatement.getConditionStatement();

								Module evaluationModule = statement.getModule();
								List<Boolean> evaluations = new ArrayList<>();

								// state comparison as enums
								if (statement.getState() != null) {
									evaluations.add(statement.getState() == evaluationModule.getState());
								}

								// value comparison
								if (statement.getValue() != null) {
									// value comparison as Double
									Double moduleValue = DoubleValidator.getInstance().validate(evaluationModule.getValue());
									if (moduleValue != null) {
										if (statement.getMinValue() != null) {
											evaluations.add(moduleValue >= statement.getMinValue());
										}
										if (statement.getMaxValue() != null) {
											evaluations.add(moduleValue <= statement.getMaxValue());
										}
										if (statement.getValue() != null) {
											Double conditionValue = DoubleValidator.getInstance().validate(statement.getValue());
											if (conditionValue != null) {
												evaluations.add(moduleValue == conditionValue);
											}
										}
									} else {
										// value comparison as String
										evaluations.add(statement.getValue().equals(evaluationModule.getValue()));
									}
								}

								// Statement evaluation
								boolean statementResult = false;
								if (!evaluations.contains(false)) {
									statementResult = true;
								}

								// Negated statement
								if (statement.getNegated() && !evaluations.isEmpty()) {
									statementResult = !statementResult;
								}

								// Add evaluated statement to andLegs
								if (!evaluations.isEmpty()) {
									andLegs.add(statementResult);
								}
							});

					orLegs.add(andLegs);
				});

		// Predicate evaluation
		for (List<Boolean> orLeg : orLegs) {
			List<Boolean> andLegs = orLeg;
			boolean disjunctiveCondition = andLegs.stream().allMatch(andLeg -> andLeg == true);
			if (disjunctiveCondition) {
				// A disjunctive condition, defining multiple conjunctive conditions, is validated!
				valid = true;
				break;
			}
		}

		return valid;
	}
}