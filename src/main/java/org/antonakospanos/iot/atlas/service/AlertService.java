package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.dao.model.Account;
import org.antonakospanos.iot.atlas.dao.model.Action;
import org.antonakospanos.iot.atlas.dao.model.Alert;
import org.antonakospanos.iot.atlas.dao.model.Condition;
import org.antonakospanos.iot.atlas.dao.repository.AccountRepository;
import org.antonakospanos.iot.atlas.dao.repository.AlertRepository;
import org.antonakospanos.iot.atlas.dao.repository.ConditionRepository;
import org.antonakospanos.iot.atlas.web.dto.alerts.AlertDto;
import org.antonakospanos.iot.atlas.web.dto.alerts.AlertRequest;
import org.antonakospanos.iot.atlas.web.dto.response.CreateResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlertService {

	private final static Logger logger = LoggerFactory.getLogger(AlertService.class);

	@Autowired
	AccountService accountService;

	@Autowired
	AlertRepository alertRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	ConditionRepository conditionRepository;

	@Autowired
	ConditionService conditionService;


	@Transactional
	public CreateResponseData create(AlertRequest request) {

		AlertDto alertDto = new AlertDto(request.getAlert());
		Account account = accountRepository.findByUsername(request.getUsername());

		if (account == null) {
			throw new IllegalArgumentException("Account with username '" + request.getUsername() + "' does not exist!");
		} else {

			// Add new Alert in DB
			Alert alert = alertDto.toEntity();
			conditionService.linkModules(alert.getCondition());
			alert.setAccount(account);

			alertRepository.save(alert);

			return new CreateResponseData(alert.getExternalId().toString());
		}
	}

	@Transactional
	public void create(Action action, Account account) {
		Condition condition = action.getCondition();

		Alert alert = new Alert();
		alert.setAccount(account);
		alert.setCondition(condition);
		condition.setAlert(alert);

		alertRepository.save(alert);
	}

	@Transactional
	public void delete(UUID alertId) {
		Alert alert = alertRepository.findByExternalId(alertId);

		if (alert == null) {
			throw new IllegalArgumentException("Alert '" + alertId + "' does not exist!");
		} else {

			Condition condition = alert.getCondition();
			condition.setAlert(null);
			conditionRepository.save(condition);

			alert.setCondition(null);
			alertRepository.delete(alert);
		}
	}

		@Transactional
	public List<AlertDto> list(String username) {
			List<AlertDto> alertDtos = new ArrayList<>();

			// Validate listed resources
			accountService.validateAccount(username);

			if (StringUtils.isNotBlank(username)) {
				// Fetch all user's alerts
				List<Alert> alerts = alertRepository.findByAccount_Username(username);
				alertDtos = alerts.stream()
						.map(alert -> new AlertDto().fromEntity(alert))
						.collect(Collectors.toList());

			} else {
				// Fetch all alerts
				List<Alert> alerts = alertRepository.findAll();
				alertDtos = alerts.stream()
						.map(alert -> new AlertDto().fromEntity(alert))
						.collect(Collectors.toList());
			}

			return alertDtos;
		}

	@Transactional
	public void validateAlert(UUID alertId) {
		if (alertId != null) {
			Alert alert = alertRepository.findByExternalId(alertId);
			if (alert == null) {
				throw new IllegalArgumentException("Alert '" + alert + "' does not exist!");
			}
		}
	}
}
