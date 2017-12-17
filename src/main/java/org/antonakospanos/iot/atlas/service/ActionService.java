package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.dao.model.Action;
import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.dao.repository.ActionRepository;
import org.antonakospanos.iot.atlas.dao.repository.DeviceRepository;
import org.antonakospanos.iot.atlas.dao.repository.ModuleRepository;
import org.antonakospanos.iot.atlas.web.dto.ModuleActionDto;
import org.antonakospanos.iot.atlas.web.dto.actions.ActionDto;
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
	ModuleRepository moduleRepository;

	@Autowired
	DeviceRepository deviceRepository;

	@Transactional
	public List<ActionDto> list(String username, String deviceId, String moduleId) {
			List<ActionDto> actionDtos = new ArrayList<>();

			if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(deviceId) && StringUtils.isNotBlank(moduleId)) {
				// Fetch all user's actions for the declared device and module
				Device device = deviceRepository.findByExternalId(deviceId);

				actionDtos = device.getModules().stream()
						.map(module -> module.getExternalId())
						.map(externalId -> actionRepository.findByAccount_Username_AndModule_ExternalId(username, externalId))
						.flatMap(List::stream)
						.map(action -> new ActionDto().fromEntity(action))
						.collect(Collectors.toList());

			} else if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(deviceId)) {
				// Fetch all user's actions for the declared device
				List<Module> modules = moduleRepository.findByDevice_ExternalId(deviceId);

				actionDtos = modules.stream()
						.map(module -> module.getExternalId())
						.map(externalId -> actionRepository.findByAccount_Username_AndModule_ExternalId(username, externalId))
						.flatMap(List::stream)
						.map(action -> new ActionDto().fromEntity(action))
						.collect(Collectors.toList());

			} else if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(moduleId)) {
				// Fetch all user's actions for the declared module
				List<Action> actions = actionRepository.findByAccount_Username_AndModule_ExternalId(username, moduleId);
				actionDtos = actions.stream()
						.map(action -> new ActionDto().fromEntity(action))
						.collect(Collectors.toList());

			} else if (StringUtils.isNotBlank(username)) {
				// Fetch all user's actions
				List<Action> actions = actionRepository.findByAccount_Username(username);
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

	@Transactional
	public List<ModuleActionDto> findActions(Device device) {

		List<ModuleActionDto> moduleActions = new ArrayList<>();

		for (Module module : device.getModules()) {
			List<Action> plannedActions = findPlannedActions(module.getId());

			if (!plannedActions.isEmpty()) {
				Optional<Action> plannedAction = plannedActions.stream()
						.sorted(Comparator.comparing(Action::getNextExecution, Comparator.reverseOrder()))
						.findFirst();

				// Add latest module's action on the response
				if (plannedAction.isPresent()) {
					Action triggeredAction = plannedAction.get();
					ModuleActionDto moduleAction = new ModuleActionDto(module.getExternalId(), triggeredAction.getState(), triggeredAction.getValue());
					moduleActions.add(moduleAction);
					logger.debug("Returned action: " + moduleAction);
				}

				rescheduleActions(plannedActions);
			}
		}

		return moduleActions;
	}

	public List<Action> findPlannedActions(Long moduleId) {
		List<Action> actions = actionRepository.findByModule_Id(moduleId);

		return actions.stream()
				.filter(a -> a.getNextExecution().isBefore(ZonedDateTime.now()))
				.collect(Collectors.toList());
	}

	/**
	 * Module's actions are marked as lazily fetched, hence may be manage themselves too.
	 *
	 * @param actions
	 */
	public void rescheduleActions(List<Action> actions) {

		for (Action action : actions) {

			if (action.getPeriodOfMinutes() != null) {
				// Schedule the action's next execution
				action.setNextExecution(action.getNextExecution().plusMinutes(action.getPeriodOfMinutes()));
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
	 * @Deprecated Replaced by ActionService#rescheduleActions(java.util.List)
	 *
	 * @param actions
	 */
	public void rescheduleActions(Module module, List<Action> actions) {

		for (Iterator<Action> iterator = actions.iterator(); iterator.hasNext(); ) {
			Action action = iterator.next();

			if (action.getPeriodOfMinutes() != null) {
				// Schedule the next action
				action.setNextExecution(action.getNextExecution().plusMinutes(action.getPeriodOfMinutes()));
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
}
