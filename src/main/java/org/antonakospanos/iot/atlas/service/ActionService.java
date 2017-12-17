package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.dao.model.Action;
import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.dao.repository.ActionRepository;
import org.antonakospanos.iot.atlas.web.dto.events.ModuleActionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActionService {

	private final static Logger logger = LoggerFactory.getLogger(ActionService.class);

	@Autowired
	ActionRepository actionRepository;

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
		List<Action> actions = actionRepository.findByModuleId(moduleId);

		return actions.stream()
				.filter(a -> a.getNextExecution().isBefore(ZonedDateTime.now()))
				.collect(Collectors.toList());
	}


	public void rescheduleActions(List<Action> actions) {

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

		if (!actions.isEmpty()) {
			actionRepository.saveAll(actions);
		}
	}
}
