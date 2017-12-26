package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.dao.model.Condition;
import org.antonakospanos.iot.atlas.dao.model.ConditionStatement;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.dao.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConditionService {

	@Autowired
	ModuleRepository moduleRepository;

	public void linkModules(Condition condition) {

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
