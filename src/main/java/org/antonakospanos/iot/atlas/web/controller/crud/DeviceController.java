package org.antonakospanos.iot.atlas.web.controller.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.antonakospanos.iot.atlas.service.DeviceService;
import org.antonakospanos.iot.atlas.support.LoggingHelper;
import org.antonakospanos.iot.atlas.web.controller.BaseAtlasController;
import org.antonakospanos.iot.atlas.web.dto.devices.DeviceDto;
import org.antonakospanos.iot.atlas.web.dto.devices.DeviceRequest;
import org.antonakospanos.iot.atlas.web.dto.response.ResponseBase;
import org.antonakospanos.iot.atlas.web.enums.Result;
import org.antonakospanos.iot.atlas.web.validator.DeviceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
	public ResponseEntity<Iterable> listAllDevices() {

		logger.debug(LoggingHelper.logInboundRequest("/devices/"));
		ResponseEntity<Iterable> devices = list(null, null);
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
	@RequestMapping(value = "/devices/{deviceId}", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<Iterable> listDevice(@PathVariable String deviceId) {

		logger.debug(LoggingHelper.logInboundRequest("/devices/" + deviceId));
		ResponseEntity<Iterable> devices = list(deviceId, null);
		logger.debug(LoggingHelper.logInboundResponse(devices));

		return devices;
	}

	@RequestMapping(value = "/devices/{deviceId}", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.PUT)
	@ApiOperation(value = "Adds or updates the information of the IoT device", response = ResponseBase.class)
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The device is replaced!", response = ResponseBase.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<ResponseBase> update(@PathVariable String deviceId, @Valid @RequestBody DeviceRequest request) {
		logger.debug(LoggingHelper.logInboundRequest(request));
		ResponseEntity<ResponseBase> response;

		DeviceDto deviceDto = new DeviceDto(deviceId, request.getDevice());
		DeviceValidator.validateDeviceRequest(deviceDto);

		service.put(deviceDto);

		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);
		response = ResponseEntity.ok().body(responseBase);

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists the integrated IoT devices", response = DeviceDto.class, responseContainer="List")
	@RequestMapping(value = "/accounts/{username}/devices/{deviceId}", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<Iterable> listAccountDevice(@PathVariable String username, @PathVariable String deviceId) {

		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + username + "/devices/" + deviceId));
		ResponseEntity<Iterable> devices = list(deviceId, username);
		logger.debug(LoggingHelper.logInboundResponse(devices));

		return devices;
	}

	private ResponseEntity<Iterable> list (String deviceId, String username) {
		ResponseEntity<Iterable> response = null;

		List<DeviceDto> devices = service.list(deviceId, username);
		if (devices != null && !devices.isEmpty()) {
			response = ResponseEntity.status(HttpStatus.OK).body(devices);
		} else {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(devices);
		}

		return response;
	}
}
