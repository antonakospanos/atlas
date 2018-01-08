package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.dao.model.Condition;
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


	/**
	 * Checked by heartbeat too
	 */
	@Scheduled(fixedRate = actionsLookupRate)
	public void actionsLookup() {
		List<Device> devices = deviceRepository.findAll();
		for (Device device : devices) {
			actionService.triggerActions(device);
		}
	}

	@Scheduled(fixedRate = actionsLookupRate)
	public void alertsLookup() {
		List<Condition> conditions = conditionService.findAllValid(); // findValidWithAlerts();
//  TODO
//		for (Condition condition : conditions) {
//	    actionService.triggerActions(condition);
// 	}
	}
}
