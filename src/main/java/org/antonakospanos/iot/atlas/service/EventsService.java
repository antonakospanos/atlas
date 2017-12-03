package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.dao.model.Action;
import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.dao.repository.DeviceRepository;
import org.antonakospanos.iot.atlas.web.dto.DeviceDto;
import org.antonakospanos.iot.atlas.web.dto.HeartbeatRequest;
import org.antonakospanos.iot.atlas.web.dto.HeartbeatResponseData;
import org.antonakospanos.iot.atlas.web.dto.ModuleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class EventsService {

	private final static Logger logger = LoggerFactory.getLogger(EventsService.class);

	@Autowired
	ActionService actionService;

	@Autowired
	DeviceRepository deviceRepository;

	public HeartbeatResponseData addEvent(HeartbeatRequest request) {
		HeartbeatResponseData responseData = null;

		DeviceDto deviceDto = request.getDevice();
		Device device = deviceRepository.findByExternalId(deviceDto.getId());

		if (device == null) {
			deviceRepository.save(deviceDto.toEntity());
			logger.info("New Device added: " + deviceDto);
		} else {
			// Update Device information in DB
			deviceRepository.save(deviceDto.toEntity(device));
			logger.debug("Device is updated: " + deviceDto);

			// Check for planned actions for device's modules
			List<ModuleDto> moduleActions = new ArrayList<>();
			for (Module module : device.getModules()) {
				List<Action> plannedActions = actionService.findPlannedActions(module.getId());

				Optional<Action> plannedAction =
				plannedActions.stream()
						.sorted(Comparator.comparing(Action::getNextExecution, Comparator.reverseOrder()))
						.findFirst();

				// Add latest module's action on the response
				if (plannedAction.isPresent()) {
					Action triggeredAction = plannedAction.get();
					ModuleDto moduleAction = new ModuleDto(module.getType(), triggeredAction.getState(), triggeredAction.getValue());
					moduleActions.add(moduleAction);
				}

				actionService.rescheduleActions(plannedActions);
			}

			responseData.setActions(moduleActions);
		}

		return responseData;
	}
}
