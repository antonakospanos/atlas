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
import java.util.UUID;

@RestController
@Api(value = "Devices API", tags = "devices", position = 3, description = "Device Management")
@RequestMapping(value = "/")
public class DeviceController extends BaseAtlasController {

	private final static Logger logger = LoggerFactory.getLogger(DeviceController.class);

	@Autowired
	DeviceService service;

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
	@RequestMapping(value = "/devices", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<Iterable> listAllDevices() {

		logger.debug(LoggingHelper.logInboundRequest("/devices/"));
		List<DeviceDto> devices = service.listAll();

		ResponseEntity<Iterable> response = createResponse(devices);
		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists the integrated IoT devices", response = DeviceDto.class, responseContainer="List")
	@RequestMapping(value = "/devices/{deviceId}", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<Iterable> listDevice(@PathVariable String deviceId) {

		logger.debug(LoggingHelper.logInboundRequest("/devices/" + deviceId));
		List<DeviceDto> devices = service.list(deviceId);

		ResponseEntity<Iterable> response = createResponse(devices);
		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists the integrated IoT devices", response = DeviceDto.class, responseContainer="List")
	@RequestMapping(value = "/accounts/{accountId}/devices", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<Iterable> listAccountDevices(@PathVariable UUID accountId) {

		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + accountId + "/devices/"));
		List<DeviceDto> devices = service.listByAccountId(null, accountId);

		ResponseEntity<Iterable> response = createResponse(devices);
		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists the integrated IoT devices", response = DeviceDto.class, responseContainer="List")
	@RequestMapping(value = "/accounts/{accountId}/devices/{deviceId}", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<Iterable> listAccountDevice(@PathVariable UUID accountId, @PathVariable String deviceId) {

		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + accountId + "/devices/" + deviceId));
		List<DeviceDto> devices = service.listByAccountId(deviceId, accountId);

		ResponseEntity<Iterable> response = createResponse(devices);
		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

//	@ApiOperation(value = "Lists the integrated IoT devices", response = DeviceDto.class, responseContainer="List")
//	@RequestMapping(value = "/accounts/{username}/devices", produces = {"application/json"},	method = RequestMethod.GET)
//	public ResponseEntity<Iterable> listAccountDevices(@PathVariable String username) {
//
//		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + username + "/devices/"));
//		List<DeviceDto> deviceDtos = service.listByUsername(null, username);
//
//		ResponseEntity<Iterable> response = createResponse(deviceDtos);
//		logger.debug(LoggingHelper.logInboundResponse(response));
//
//		return response;
//	}
//
// 	@ApiOperation(value = "Lists the integrated IoT devices", response = DeviceDto.class, responseContainer="List")
//	@RequestMapping(value = "/accounts/{username}/devices/{deviceId}", produces = {"application/json"},	method = RequestMethod.GET)
//	public ResponseEntity<Iterable> listAccountDevice(@PathVariable String username, @PathVariable String deviceId) {
//
//		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + username + "/devices/" + deviceId));
//	  List<DeviceDto> deviceDtos = service.listByUsername(deviceId, username);
//
//	  ResponseEntity<Iterable> response = createResponse(deviceDtos);
//		logger.debug(LoggingHelper.logInboundResponse(response));
//
//		return response;
//	}

	private ResponseEntity<Iterable> createResponse(List<DeviceDto> devices) {
		ResponseEntity<Iterable> response;

		if (devices != null && !devices.isEmpty()) {
			response = ResponseEntity.status(HttpStatus.OK).body(devices);
		} else {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(devices);
		}

		return response;
	}
}
