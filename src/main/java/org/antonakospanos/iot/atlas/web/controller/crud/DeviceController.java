package org.antonakospanos.iot.atlas.web.controller.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.antonakospanos.iot.atlas.service.DeviceService;
import org.antonakospanos.iot.atlas.support.LoggingHelper;
import org.antonakospanos.iot.atlas.web.controller.BaseAtlasController;
import org.antonakospanos.iot.atlas.web.dto.DeviceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "Devices API", tags = "devices", position = 3, description = "Device Management")
@RequestMapping(value = "/")
public class DeviceController extends BaseAtlasController {

	private final static Logger logger = LoggerFactory.getLogger(DeviceController.class);

	@Autowired
	DeviceService service;

	@ApiOperation(value = "Lists the integrated IoT devices", response = DeviceDto.class, responseContainer="List")
	@RequestMapping(value = "/devices", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<Iterable> listDevices() {
		logger.debug(LoggingHelper.logInboundRequest("/devices/"));

		ResponseEntity<Iterable> devices = list(null, null);

		logger.debug(LoggingHelper.logInboundResponse(devices));

		return devices;
	}

	@ApiOperation(value = "Lists the integrated IoT devices", response = DeviceDto.class, responseContainer="List")
	@RequestMapping(value = "/devices/{deviceId}", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<Iterable> listDevices(@PathVariable String deviceId) {
		logger.debug(LoggingHelper.logInboundRequest("/devices/" + deviceId));

		ResponseEntity<Iterable> devices = list(deviceId, null);

		logger.debug(LoggingHelper.logInboundResponse(devices));

		return devices;
	}

	@ApiOperation(value = "Lists the integrated IoT devices", response = DeviceDto.class, responseContainer="List")
	@RequestMapping(value = "/accounts/{username}/devices", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<Iterable> listAccountDevices(@PathVariable String username) {
		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + username + "/devices/"));

		ResponseEntity<Iterable> devices = list(null, username);

		logger.debug(LoggingHelper.logInboundResponse(devices));

		return devices;
	}

	@ApiOperation(value = "Lists the integrated IoT devices", response = DeviceDto.class, responseContainer="List")
	@RequestMapping(value = "/accounts/{username}/devices/{deviceId}", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<Iterable> listAccountDevices(@PathVariable String username, @PathVariable (required=false) String deviceId) {
		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + username + "/devices/" + deviceId));

		ResponseEntity<Iterable> devices = list(deviceId, username);

		logger.debug(LoggingHelper.logInboundResponse(devices));

		return devices;
	}

	private ResponseEntity<Iterable> list (String deviceId, String username) {
		ResponseEntity<Iterable> response = null;

		try {
			List<DeviceDto> devices = service.list(deviceId, username);

			if (devices != null && !devices.isEmpty()) {
				response = ResponseEntity.status(HttpStatus.OK).body(devices);
			} else {
				response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(devices);
			}
		} catch (Exception e) {
			logger.error(e.getClass() + " Cause: " + e.getCause() + " Message: " + e.getMessage(), e);
		}

		return response;
	}
}
