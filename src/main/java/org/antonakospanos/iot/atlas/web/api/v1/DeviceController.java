package org.antonakospanos.iot.atlas.web.api.v1;

import io.swagger.annotations.*;
import org.antonakospanos.iot.atlas.service.DeviceService;
import org.antonakospanos.iot.atlas.support.ControllerUtils;
import org.antonakospanos.iot.atlas.support.LoggingHelper;
import org.antonakospanos.iot.atlas.web.api.BaseAtlasController;
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
@RequestMapping(value = "/api/")
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

		service.put(deviceDto, true);

		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);
		response = ResponseEntity.ok().body(responseBase);

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists the integrated IoT devices", response = DeviceDto.class, responseContainer="List")
	@RequestMapping(value = "/devices", produces = {"application/json"},	method = RequestMethod.GET)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	public ResponseEntity<Iterable> listAllDevices() {

		logger.debug(LoggingHelper.logInboundRequest("/devices/"));
		List<DeviceDto> devices = service.listAll();

		ResponseEntity<Iterable> response = ControllerUtils.listResources(devices);
		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists the integrated IoT devices", response = DeviceDto.class)
	@RequestMapping(value = "/devices/{deviceId}", produces = {"application/json"},	method = RequestMethod.GET)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	public ResponseEntity<DeviceDto> listDevice(@PathVariable String deviceId) {

		logger.debug(LoggingHelper.logInboundRequest("/devices/" + deviceId));
		List<DeviceDto> devices = service.list(deviceId);

		ResponseEntity<DeviceDto> response = ControllerUtils.listResource(devices);
		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}
}
