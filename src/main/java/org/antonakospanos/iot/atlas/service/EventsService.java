package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.dao.model.Action;
import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.dao.repository.DeviceRepository;
import org.antonakospanos.iot.atlas.dao.repository.ModuleRepository;
import org.antonakospanos.iot.atlas.web.dto.DeviceDto;
import org.antonakospanos.iot.atlas.web.dto.HeartbeatRequest;
import org.antonakospanos.iot.atlas.web.dto.HeartbeatResponseData;
import org.antonakospanos.iot.atlas.web.dto.ModuleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

	@Autowired
	ModuleRepository moduleRepository;

	@Transactional
	public HeartbeatResponseData addEvent(HeartbeatRequest request) {
		HeartbeatResponseData responseData = new HeartbeatResponseData();

		DeviceDto deviceDto = request.getDevice();
		Device device = deviceRepository.findByExternalId(deviceDto.getId());

		if (device == null) {
			// Add new Device in DB
			device = deviceDto.toEntity();

			deviceRepository.save(device);

			logger.info("New Device added: " + deviceDto);

		} else {
			// Update Device information in DB
			device = deviceDto.toEntity();

			deviceRepository.save(device);
			logger.debug("Device is updated: " + deviceDto);

			// Check for planned actions for device's modules
			List<ModuleDto> moduleActions = new ArrayList<>();
			for (Module module : device.getModules()) {
				List<Action> plannedActions = actionService.findPlannedActions(module.getId());

				if (!plannedActions.isEmpty()) {

					Optional<Action> plannedAction = plannedActions.stream()
									.sorted(Comparator.comparing(Action::getNextExecution, Comparator.reverseOrder()))
									.findFirst();

					// Add latest module's action on the response
					if (plannedAction.isPresent()) {
						Action triggeredAction = plannedAction.get();
						ModuleDto moduleAction = new ModuleDto(module.getType(), triggeredAction.getState(), triggeredAction.getValue());
						moduleActions.add(moduleAction);
						logger.debug("Returned action: " + moduleAction);
					}

					actionService.rescheduleActions(plannedActions);
				}
			}

			responseData.setActions(moduleActions);
		}

		return responseData;
	}
}
