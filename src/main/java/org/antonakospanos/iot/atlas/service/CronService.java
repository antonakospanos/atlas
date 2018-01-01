package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.broker.mqtt.producer.ActionProducer;
import org.antonakospanos.iot.atlas.broker.mqtt.producer.AlertProducer;
import org.antonakospanos.iot.atlas.dao.repository.AccountRepository;
import org.antonakospanos.iot.atlas.dao.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronService {

	@Value("${application.cron.actions.lookup-rate}")
	public final int actionsLookupRate = 10000;

	@Value("${application.cron.alerts.lookup-rate}")
	public final int alertsLookupRate = 10000;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	ActionService actionService;

	@Autowired
	ConditionService conditionService;

	@Autowired
	ActionProducer actionProducer;

	@Autowired
	AlertProducer alertProducer;


	/**
	 * @Deprecated
	 * Checked by heartbeat at the moment
	 */
	@Scheduled(fixedRate = actionsLookupRate)
	@Deprecated
	public void actionsLookup() {
//		List<Device> devices = deviceRepository.findAll();
//		for (Device device : devices) {
//			List<ModuleActionDto> actions = actionService.findActions(device);
//			actionProducer.publishAction(actions);
//		}
	}

	@Scheduled(fixedRate = actionsLookupRate)
	public void alertsLookup() {
//		List<Condition> conditions = conditionService.findValidWithAlerts();
//
//		for (Condition condition : conditions) {
//			Alert alert = condition.getAlert();
//			// TODO: Create user's alert
//			alertProducer.publish(null);
//		}
	}
}
