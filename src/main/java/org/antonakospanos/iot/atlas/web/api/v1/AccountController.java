package org.antonakospanos.iot.atlas.web.api.v1;

import io.swagger.annotations.*;
import javassist.NotFoundException;
import org.antonakospanos.iot.atlas.service.AccountService;
import org.antonakospanos.iot.atlas.service.DeviceService;
import org.antonakospanos.iot.atlas.support.ControllerUtils;
import org.antonakospanos.iot.atlas.support.LoggingHelper;
import org.antonakospanos.iot.atlas.web.api.BaseAtlasController;
import org.antonakospanos.iot.atlas.web.dto.IdentityDto;
import org.antonakospanos.iot.atlas.web.dto.accounts.AccountCreateRequest;
import org.antonakospanos.iot.atlas.web.dto.accounts.AccountDto;
import org.antonakospanos.iot.atlas.web.dto.accounts.AccountUpdateRequest;
import org.antonakospanos.iot.atlas.web.dto.devices.DeviceDto;
import org.antonakospanos.iot.atlas.web.dto.patch.PatchRequest;
import org.antonakospanos.iot.atlas.web.dto.response.CreateResponse;
import org.antonakospanos.iot.atlas.web.dto.response.CreateResponseData;
import org.antonakospanos.iot.atlas.web.dto.response.ResponseBase;
import org.antonakospanos.iot.atlas.web.enums.Result;
import org.antonakospanos.iot.atlas.web.validator.AccountsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@Api(value = "Accounts API", tags = "accounts", position = 2, description = "Account Management")
@RequestMapping(value = "/api/accounts")
public class AccountController extends BaseAtlasController {

	private final static Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	AccountService service;

	@Autowired
	DeviceService deviceService;

	@RequestMapping(value = "", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.POST)
	@ApiOperation(value = "Creates the account of the user owning an IoT device", response = CreateResponse.class)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The account is created!", response = CreateResponse.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<CreateResponse> create(UriComponentsBuilder uriBuilder, @Valid @RequestBody AccountCreateRequest request) {
		ResponseEntity<CreateResponse> response;
		logger.debug(LoggingHelper.logInboundRequest(request));

		CreateResponseData data = service.create(request);
		UriComponents uriComponents =	uriBuilder.path("/accounts/{id}").buildAndExpand(data.getId());
		CreateResponse responseBase = CreateResponse.Builder().build(Result.SUCCESS).data(data);
		response = ResponseEntity.created(uriComponents.toUri()).body(responseBase);

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@RequestMapping(value = "/{accountId}", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.PUT)
	@ApiOperation(value = "Replaces the account of the user owning an IoT device", response = ResponseBase.class)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The account is replaced!", response = ResponseBase.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<ResponseBase> replace(@PathVariable UUID accountId, @Valid @RequestBody AccountUpdateRequest request) {
		ResponseEntity<ResponseBase> response;
		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + accountId + "\n" + request));

		service.replace(accountId, request);
		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);
		response = ResponseEntity.ok().body(responseBase);

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@RequestMapping(value = "/{accountId}", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.PATCH)
	@ApiOperation(value = "Updates the account of the user owning an IoT device", response = ResponseBase.class)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The account is updated!", response = ResponseBase.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<ResponseBase> update(@PathVariable UUID accountId, @Valid @RequestBody PatchRequest request) {
		ResponseEntity<ResponseBase> response;
		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + accountId + "\n" + request));

		AccountsValidator.validateAccount(request);

		CreateResponseData data = service.update(accountId, request.getPatches());
		CreateResponse responseBase = CreateResponse.Builder().build(Result.SUCCESS).data(data);
		response = ResponseEntity.ok().body(responseBase);

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Deletes the user's account owning an IoT device", response = ResponseBase.class)
	@RequestMapping(value = "/{accountId}", produces = {"application/json"},	method = RequestMethod.DELETE)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseBase> delete(@PathVariable UUID accountId) {
		ResponseEntity<ResponseBase> response;
		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + accountId));

		service.delete(accountId);
		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);
		response = ResponseEntity.status(HttpStatus.OK).body(responseBase);

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists the authenticated user's ID", response = IdentityDto.class)
	@RequestMapping(value = "/id", produces = {"application/json"},	method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The user is authenticated", response = IdentityDto.class),
			@ApiResponse(code = 401, message = "The credentials are invalid"),
			@ApiResponse(code = 404, message = "No user found!"),
			@ApiResponse(code = 500, message = "Server Error")})
	public ResponseEntity<IdentityDto> listAccount(UriComponentsBuilder uriBuilder, @RequestParam String username, @RequestParam String password) throws NotFoundException {

		logger.debug(LoggingHelper.logInboundRequest("/accounts?username=" + username + "?password=" + password));

		ResponseEntity<IdentityDto> response;
		AccountDto account = service.find(username, password);

		if (account == null) {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} else {
			IdentityDto identityDto = new IdentityDto(account.getId().toString());
			UriComponents uriComponents = uriBuilder.path("/accounts/{id}").buildAndExpand(identityDto.getId());
			response = ResponseEntity.status(HttpStatus.OK).location(uriComponents.toUri()).body(identityDto);
		}

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists all accounts of the integrated IoT devices", response = AccountDto.class, responseContainer="List")
	@RequestMapping(value = "", produces = {"application/json"},	method = RequestMethod.GET)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	public ResponseEntity<Iterable> listAllAccounts() {

		logger.debug(LoggingHelper.logInboundRequest("/accounts"));
		ResponseEntity<Iterable> response = listAll();
		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists the accounts of the integrated IoT devices", response = AccountDto.class)
	@RequestMapping(value = "/{accountId}", produces = {"application/json"},	method = RequestMethod.GET)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	public ResponseEntity<AccountDto> listAccount(@PathVariable UUID accountId) {

		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + accountId));
		ResponseEntity<AccountDto> response = list(accountId);
		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

//	@ApiOperation(value = "Lists the accounts of the integrated IoT devices", response = AccountDto.class, responseContainer="List")
//	@RequestMapping(value = "/{username}", produces = {"application/json"},	method = RequestMethod.GET)
//	@ApiImplicitParam(
//			name = "Authorization",
//			value = "Bearer <The user's access token obtained upon registration or authentication>",
//			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
//			required = true,
//			dataType = "string",
//			paramType = "header"
//	)
//	public ResponseEntity<Iterable> listAccount(@PathVariable String username) {
//
//		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + username));
//		ResponseEntity<Iterable> response = list(username);
//		logger.debug(LoggingHelper.logInboundResponse(response));
//
//		return response;
//	}

//	@RequestMapping(value = "/{username}", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.PUT)
//	@ApiOperation(value = "Replaces the account of the user owning an IoT device", response = ResponseBase.class)
//	@ApiImplicitParam(
//			name = "Authorization",
//			value = "Bearer <The user's access token obtained upon registration or authentication>",
//			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
//			required = true,
//			dataType = "string",
//			paramType = "header"
//	)
//	@ResponseStatus(HttpStatus.OK)
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = "The account is replaced!", response = ResponseBase.class),
//			@ApiResponse(code = 400, message = "The request is invalid!"),
//			@ApiResponse(code = 500, message = "server error")})
//	public ResponseEntity<ResponseBase> replace(@PathVariable String username, @Valid @RequestBody AccountCreateRequest request) {
//		ResponseEntity<ResponseBase> response;
//		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + accountId));
//
//		AccountsValidator.validateAccount(request);
//
//		service.replace(username, request);
//		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);
//		response = ResponseEntity.ok().body(responseBase);
//
//		logger.debug(LoggingHelper.logInboundResponse(response));
//
//		return response;
//	}
//
//	@RequestMapping(value = "/{username}", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.PATCH)
//	@ApiOperation(value = "Updates the account of the user owning an IoT device", response = ResponseBase.class)
//	@ApiImplicitParam(
//			name = "Authorization",
//			value = "Bearer <The user's access token obtained upon registration or authentication>",
//			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
//			required = true,
//			dataType = "string",
//			paramType = "header"
//	)
//	@ResponseStatus(HttpStatus.OK)
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = "The account is updated!", response = ResponseBase.class),
//			@ApiResponse(code = 400, message = "The request is invalid!"),
//			@ApiResponse(code = 500, message = "server error")})
//	public ResponseEntity<ResponseBase> update(@PathVariable String username, @Valid @RequestBody PatchRequest request) {
//		ResponseEntity<ResponseBase> response;
//		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + accountId));
//
//		AccountsValidator.validateAccount(request);
//
//		service.update(username, request.getPatches());
//		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);
//		response = ResponseEntity.ok().body(responseBase);
//
//		logger.debug(LoggingHelper.logInboundResponse(response));
//
//		return response;
//	}
//
//	@ApiOperation(value = "Deletes the user's account owning an IoT device", response = ResponseBase.class)
//	@RequestMapping(value = "/{username}", produces = {"application/json"},	method = RequestMethod.DELETE)
//	@ApiImplicitParam(
//			name = "Authorization",
//			value = "Bearer <The user's access token obtained upon registration or authentication>",
//			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
//			required = true,
//			dataType = "string",
//			paramType = "header"
//	)
//	@ResponseBody
//	@ResponseStatus(HttpStatus.OK)
//	public ResponseEntity<ResponseBase> delete(@PathVariable String username) {
//		ResponseEntity<ResponseBase> response;
//		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + username));
//
//		service.delete(username);
//		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);
//		response = ResponseEntity.status(HttpStatus.OK).body(responseBase);
//
//		logger.debug(LoggingHelper.logInboundResponse(response));
//
//		return response;
//	}

	@RequestMapping(value = "/{accountId}/devices/{deviceId}", produces = {"application/json"}, method = RequestMethod.POST)
	@ApiOperation(value = "Adds an IoT device to the user's account", response = CreateResponse.class)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The device is added!", response = CreateResponse.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<CreateResponse> addDevice(@PathVariable UUID accountId, @PathVariable String deviceId) {
		service.validateAccount(accountId);

		service.addDevice(accountId, deviceId);
		CreateResponse responseBase = CreateResponse.Builder().build(Result.SUCCESS);

		return ResponseEntity.ok().body(responseBase);
	}

	@ApiOperation(value = "Lists the user's integrated IoT devices", response = DeviceDto.class, responseContainer="List")
	@RequestMapping(value = "/{accountId}/devices", produces = {"application/json"},	method = RequestMethod.GET)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	public ResponseEntity<Iterable> listAccountDevices(@PathVariable UUID accountId) {
		List<DeviceDto> devices = deviceService.listByAccountId(null, accountId);

		return ControllerUtils.listResources(devices);
	}

	@ApiOperation(value = "Lists the user's integrated IoT device", response = DeviceDto.class)
	@RequestMapping(value = "/{accountId}/devices/{deviceId}", produces = {"application/json"},	method = RequestMethod.GET)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	public ResponseEntity<DeviceDto> listAccountDevice(@PathVariable UUID accountId, @PathVariable String deviceId) {
		List<DeviceDto> devices = deviceService.listByAccountId(deviceId, accountId);

		return ControllerUtils.listResource(devices);
	}

//	@ApiOperation(value = "Lists the integrated IoT devices", response = DeviceDto.class, responseContainer="List")
//	@RequestMapping(value = "/accounts/{username}/devices", produces = {"application/json"},	method = RequestMethod.GET)
//	@ApiImplicitParam(
//			name = "Authorization",
//			value = "Bearer <The user's access token obtained upon registration or authentication>",
//			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
//			required = true,
//			dataType = "string",
//			paramType = "header"
//	)
//	public ResponseEntity<Iterable> listAccountDevices(@PathVariable String username) {
//
//		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + username + "/devices/"));
//		List<DeviceDto> deviceDtos = service.listByUsername(null, username);
//
//		ResponseEntity<Iterable> response = ControllerUtils.listResources(deviceDtos);
//		logger.debug(LoggingHelper.logInboundResponse(response));
//
//		return response;
//	}
//
// 	@ApiOperation(value = "Lists the integrated IoT devices", response = DeviceDto.class, responseContainer="List")
//	@RequestMapping(value = "/accounts/{username}/devices/{deviceId}", produces = {"application/json"},	method = RequestMethod.GET)
//	@ApiImplicitParam(
//			name = "Authorization",
//			value = "Bearer <The user's access token obtained upon registration or authentication>",
//			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
//			required = true,
//			dataType = "string",
//			paramType = "header"
//	)
//	public ResponseEntity<Iterable> listAccountDevice(@PathVariable String username, @PathVariable String deviceId) {
//
//		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + username + "/devices/" + deviceId));
//	  List<DeviceDto> deviceDtos = service.listByUsername(deviceId, username);
//
//	  ResponseEntity<DeviceDto> response = ControllerUtils.listResource(deviceDtos);
//		logger.debug(LoggingHelper.logInboundResponse(response));
//
//		return response;
//	}


	private ResponseEntity<Iterable> listAll() {
		List<AccountDto> accounts = service.listAll();

		return ControllerUtils.listResources(accounts);
	}

	private ResponseEntity<AccountDto> list(String username) {
		List<AccountDto> accounts = service.list(username);

		return ControllerUtils.listResource(accounts);
	}

	private ResponseEntity<AccountDto> list(UUID accountId) {
		List<AccountDto> accounts = service.list(accountId);

		return ControllerUtils.listResource(accounts);
	}
}
