package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.dao.converter.DeviceConverter;
import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.dao.repository.DeviceRepository;
import org.antonakospanos.iot.atlas.dao.repository.ModuleRepository;
import org.antonakospanos.iot.atlas.web.dto.ModuleActionDto;
import org.antonakospanos.iot.atlas.web.dto.events.HeartbeatRequest;
import org.antonakospanos.iot.atlas.web.dto.events.HeartbeatResponseData;
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

	@Autowired
	DeviceService deviceService;


	@Transactional
	public HeartbeatResponseData create(HeartbeatRequest request) {
		Device device = deviceService.put(request.getDevice(), false);

		// Publish any planned or conditional actions for devices's modules
		// TODO: Improve action lookup. Should be checked only conditional actions. The rest will be triggered from the scheduler
		List<ModuleActionDto> actions = actionService.triggerPlannedActions(device);

		HeartbeatResponseData responseData = new HeartbeatResponseData();
		responseData.setActions(actions);

		return responseData;
	}
}
