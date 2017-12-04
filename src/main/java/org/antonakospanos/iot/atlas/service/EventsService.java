package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.dao.converter.DeviceConverter;
import org.antonakospanos.iot.atlas.dao.model.Device;
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
import java.util.List;

@Service
public class EventsService {

	private final static Logger logger = LoggerFactory.getLogger(EventsService.class);

	@Autowired
	ActionService actionService;

	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	ModuleRepository moduleRepository;

	@Autowired
	DeviceConverter deviceConverter;

	@Transactional
	public HeartbeatResponseData addEvent(HeartbeatRequest request) {
		HeartbeatResponseData responseData = new HeartbeatResponseData();

		DeviceDto deviceDto = request.getDevice();
		Device device = deviceRepository.findByExternalId(deviceDto.getId());

		if (device == null) {
			// Add new Device in DB
			device = deviceDto.toEntity(); // deviceConverter.createDevice(deviceDto);
			deviceRepository.save(device);

			logger.info("New Device added: " + deviceDto);

		} else {
			// Update Device information in DB
			deviceConverter.updateDevice(deviceDto, device);
			deviceRepository.save(device);
			logger.debug("Device is updated: " + deviceDto);

			// Check for planned actions for device's modules
			List<ModuleDto> moduleActions = actionService.findActions(device);
			responseData.setActions(moduleActions);
		}

		return responseData;
	}
}
