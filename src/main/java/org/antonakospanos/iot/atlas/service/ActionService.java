package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.adapter.mqtt.producer.ActionProducer;
import org.antonakospanos.iot.atlas.dao.model.*;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.dao.repository.*;
import org.antonakospanos.iot.atlas.web.dto.ModuleActionDto;
import org.antonakospanos.iot.atlas.web.dto.actions.ActionDto;
import org.antonakospanos.iot.atlas.web.dto.actions.ActionRequest;
import org.antonakospanos.iot.atlas.web.dto.response.CreateResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActionService {

	private final static Logger logger = LoggerFactory.getLogger(ActionService.class);

	@Autowired
	ActionRepository actionRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	ModuleRepository moduleRepository;

	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	AlertRepository alertRepository;

	@Autowired
	ConditionRepository conditionRepository;


	@Autowired
	DeviceService deviceService;

	@Autowired
	AccountService accountService;

	@Autowired
	ConditionService conditionService;

	@Autowired
	AlertService alertService;


	@Autowired
	ActionProducer actionProducer;


	@Transactional
	public CreateResponseData create(ActionRequest request) {

		ActionDto actionDto = new ActionDto(request.getAction());
		String actionDeviceId = actionDto.getDevice().getId();
		String actionModuleId = actionDto.getDevice().getModule().getId();

		Device device = deviceRepository.findByExternalId(actionDeviceId);
		Module module = moduleRepository.findByExternalId_AndDevice_ExternalId(actionModuleId, actionDeviceId);
		Account account = accountRepository.findByExternalId(request.getAccountId());

		if (device == null) {
			throw new IllegalArgumentException("Device '" + actionDeviceId + "' does not exist!");
		} else if (module == null) {
			throw new IllegalArgumentException("Module '" + actionModuleId + "' does not exist!");
		} else if (account == null) {
			throw new IllegalArgumentException("Account with accountId '" + request.getAccountId() + "' does not exist!");
		} else {

			// Add new Action in DB
			Action action = actionDto.toEntity();
			conditionService.linkModules(action.getCondition());
			action.setAccount(account);
			action.setModule(module);

			actionRepository.save(action);

			// Add new Alert in DB
			if (request.getAlert()) {
				alertService.create(action, account);
			}

			return new CreateResponseData(action.getExternalId().toString());
		}
	}

	@Transactional
	public void delete(UUID actionId, boolean deleteAlert) {
		Action action = actionRepository.findByExternalId(actionId);

		if (action == null) {
			throw new IllegalArgumentException("Action '" + actionId + "' does not exist!");
		} else {

			// Remove action-condition relationship
			Condition condition = action.getCondition();
			if (condition != null) {
				condition.setAction(null);
				conditionRepository.save(condition);
				action.setCondition(null);
			}

			// Delete Action and related Alert
			actionRepository.delete(action);

			Alert alert = condition.getAlert();
			if (deleteAlert && alert != null) {
				alertRepository.delete(alert);
			}
		}
	}

	@Transactional
	public List<ActionDto> list(UUID accountId, String deviceId, String moduleId) {
		List<ActionDto> actionDtos;

		// Validate listed resources
		deviceService.validateDevice(deviceId);
		accountService.validateAccount(accountId);

		if (accountId != null && StringUtils.isNotBlank(deviceId) && StringUtils.isNotBlank(moduleId)) {
			// Fetch all user's actions for the declared device and module
			Device device = deviceRepository.findByExternalId(deviceId);

			actionDtos = device.getModules().stream()
					.map(module -> module.getExternalId())
					.filter(moduleExternalId -> moduleId.equals(moduleExternalId))
					.map(moduleExternalId -> actionRepository.findByAccount_ExternalId_AndModule_ExternalId(accountId, moduleExternalId))
					.flatMap(List::stream)
					.map(action -> new ActionDto().fromEntity(action))
					.collect(Collectors.toList());

		} else if (accountId != null && StringUtils.isNotBlank(deviceId)) {
			// Fetch all user's actions for the declared device
			List<Module> modules = moduleRepository.findByDevice_ExternalId(deviceId);

			actionDtos = modules.stream()
					.map(module -> module.getExternalId())
					.map(moduleExternalId -> actionRepository.findByAccount_ExternalId_AndModule_ExternalId(accountId, moduleExternalId))
					.flatMap(List::stream)
					.map(action -> new ActionDto().fromEntity(action))
					.collect(Collectors.toList());

		} else if (accountId != null && StringUtils.isNotBlank(moduleId)) {
			// Fetch all user's actions for the declared module
			List<Action> actions = actionRepository.findByAccount_ExternalId_AndModule_ExternalId(accountId, moduleId);
			actionDtos = actions.stream()
					.map(action -> new ActionDto().fromEntity(action))
					.collect(Collectors.toList());

		} else if (accountId != null) {
			// Fetch all user's actions
			List<Action> actions = actionRepository.findByAccount_ExternalId(accountId);
			actionDtos = actions.stream()
					.map(action -> new ActionDto().fromEntity(action))
					.collect(Collectors.toList());

		} else {
			// Fetch all actions
			List<Action> actions = actionRepository.findAll();
			actionDtos = actions.stream()
					.map(action -> new ActionDto().fromEntity(action))
					.collect(Collectors.toList());
		}

		return actionDtos;
	}

	public List<ModuleActionDto> triggerConditionalActions() {
		//TODO: Triggered by heartbeat!
		return null;
	}

	/**
	 * Checks for planned or conditional actions for devices's modules
	 * and publishes them to the MQTT Broker
	 *
	 * @param device
	 * @return The published actions
	 */
	public List<ModuleActionDto> triggerPlannedActions(Device device) {
		List<ModuleActionDto> actions = findActions(device);

		actionProducer.publish(actions, device.getExternalId());
		alertService.triggerAlerts(actions);

		return actions;
	}

	@Transactional
	public List<ModuleActionDto> findActions(Device device) {

		List<ModuleActionDto> moduleActions = new ArrayList<>();

		for (Module module : device.getModules()) {
			List<Action> actions = findActions(module.getId());

			if (!actions.isEmpty()) {
				List<Action> plannedActions = actions.stream().filter(action -> action.getNextExecution() != null).collect(Collectors.toList());
				List<Action> conditionalActions = actions.stream().filter(action -> action.getNextExecution() == null).collect(Collectors.toList());

				// Conditional actions
				conditionalActions
						.forEach(conditionalAction -> {
							ModuleActionDto moduleAction = new ModuleActionDto(module.getExternalId(), conditionalAction);
							moduleActions.add(moduleAction);
							logger.debug("Triggered for device '" + device.getExternalId() + "' action: " + moduleAction);
						});

				// Time based actions (condition may also have been added)
				plannedActions.stream()
						.sorted(Comparator.comparing(Action::getNextExecution, Comparator.reverseOrder()))
						.forEach(plannedAction -> {
							ModuleActionDto moduleAction = new ModuleActionDto(module.getExternalId(), plannedAction);
							moduleActions.add(moduleAction);
							logger.debug("Triggered for device '" + device.getExternalId() + "' action: " + moduleAction);
						});

				rescheduleActions(actions);
			}
		}

		return moduleActions;
	}

	public List<Action> findActions(Long moduleId) {
		List<Action> actions = actionRepository.findByModule_Id(moduleId);

		return actions.stream()
				.filter(a -> a.getNextExecution() == null || a.getNextExecution().isBefore(ZonedDateTime.now()))
				.filter(a -> conditionService.isValid(a.getCondition()))
				.collect(Collectors.toList());
	}

	/**
	 * Module's actions are marked as lazily fetched, hence may be manage themselves too.
	 *
	 * @param actions
	 */
	public void rescheduleActions(List<Action> actions) {

		for (Action action : actions) {

			if (action.getPeriodInSecods() != null) {
				// Schedule the action's next execution
				action.setNextExecution(action.getNextExecution().plusSeconds(action.getPeriodInSecods()));
				actionRepository.save(action);
				logger.debug("Rescheduled action: " + action);
			} else {
				// Remove the action
				actionRepository.delete(action);
				logger.debug("Removed action: " + action);
			}
		}
	}

	/**
	 * Module's actions are marked as CascadeType.ALL and eagerly fetched, hence shall be totally managed by Module entity.
	 *
	 * @param actions
	 * @Deprecated Replaced by ActionService#rescheduleActions(java.util.List)
	 */
	public void rescheduleActions(Module module, List<Action> actions) {

		for (Iterator<Action> iterator = actions.iterator(); iterator.hasNext(); ) {
			Action action = iterator.next();

			if (action.getPeriodInSecods() != null) {
				// Schedule the next action
				action.setNextExecution(action.getNextExecution().plusSeconds(action.getPeriodInSecods()));
				logger.debug("Rescheduled action: " + action);
			} else {
				// Remove the action from the iterator and the list
				iterator.remove();
				logger.debug("Removed action: " + action);
			}
		}

		module.setActions(actions);
		moduleRepository.save(module);
	}

	@Transactional
	public void validateAction(UUID actionId) {
		if (actionId != null) {
			Action action = actionRepository.findByExternalId(actionId);
			if (action == null) {
				throw new IllegalArgumentException("Action '" + action + "' does not exist!");
			}
		}
	}
}
