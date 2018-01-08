package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.dao.repository.AccountRepository;
import org.antonakospanos.iot.atlas.dao.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

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
	AlertService alertService;

	@Autowired
	ConditionService conditionService;


	@Scheduled(fixedRate = actionsLookupRate)
	public void actionsLookup() {
		// TODO: Improve algorithm to check only planned actions. The conditional ones will be checked by the heartbeats.
		List<Device> devices = deviceRepository.findAll();
		for (Device device : devices) {
			actionService.triggerPlannedActions(device);
		}
	}

//	@Scheduled(fixedRate = actionsLookupRate)
//	public void alertsLookup() {
//		List<Condition> conditions = conditionService.findAllValid(); // findValidWithAlerts();
//		for (Condition condition : conditions) {
//	    actionService.triggerPlannedActions(condition);
// 	}
//	}
}
