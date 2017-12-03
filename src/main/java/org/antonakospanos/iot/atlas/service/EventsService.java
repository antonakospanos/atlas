package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.dao.model.Action;
import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.dao.repository.ActionRepository;
import org.antonakospanos.iot.atlas.dao.repository.DeviceRepository;
import org.antonakospanos.iot.atlas.web.dto.DeviceDto;
import org.antonakospanos.iot.atlas.web.dto.HeartbeatRequest;
import org.antonakospanos.iot.atlas.web.dto.HeartbeatResponseData;
import org.antonakospanos.iot.atlas.web.dto.ModuleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class EventsService {

	private final static Logger logger = LoggerFactory.getLogger(EventsService.class);

	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	ActionRepository actionRepository;

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

			// Check for planned actions in DB
			List<ModuleDto> moduleActions = new ArrayList<>();
			for (Module module : device.getModules()) {
				List<Action> actions = actionRepository.findByModuleId(module.getId());

				Optional<Action> action =
				actions.stream()
						.filter(a -> a.getNextExecution().isBefore(ZonedDateTime.now()))
						.sorted(Comparator.comparing(Action::getNextExecution, Comparator.reverseOrder()))
						.findFirst();

				if (action.isPresent()) {
					ModuleDto moduleAction = new ModuleDto(module.getType(), action.get().getState(), action.get().getValue());
					moduleActions.add(moduleAction);
				}
			}

			responseData.setActions(moduleActions);
		}

		return responseData;
	}
}
