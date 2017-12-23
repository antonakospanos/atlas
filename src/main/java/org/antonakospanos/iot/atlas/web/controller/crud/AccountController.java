package org.antonakospanos.iot.atlas.web.controller.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.antonakospanos.iot.atlas.service.AccountService;
import org.antonakospanos.iot.atlas.support.LoggingHelper;
import org.antonakospanos.iot.atlas.web.controller.BaseAtlasController;
import org.antonakospanos.iot.atlas.web.dto.accounts.AccountDto;
import org.antonakospanos.iot.atlas.web.dto.accounts.AccountRequest;
import org.antonakospanos.iot.atlas.web.dto.response.CreateResponse;
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

@RestController
@Api(value = "Accounts API", tags = "accounts", position = 2, description = "Account Management")
@RequestMapping(value = "/accounts")
public class AccountController extends BaseAtlasController {

	private final static Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	AccountService service;

	@RequestMapping(value = "", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.POST)
	@ApiOperation(value = "Creates the account of the user owning an IoT device", response = CreateResponse.class)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The account is created!", response = CreateResponse.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<ResponseBase> create(UriComponentsBuilder uriBuilder, @Valid @RequestBody AccountRequest request) {
		ResponseEntity<ResponseBase> response;
		logger.debug(LoggingHelper.logInboundRequest(request));

		AccountsValidator.validateAccount(request);

		service.create(request);
		UriComponents uriComponents =	uriBuilder.path("/{username}").buildAndExpand(request.getAccount().getUsername());
		ResponseBase createResponse = ResponseBase.Builder().build(Result.SUCCESS);
		response = ResponseEntity.created(uriComponents.toUri()).body(createResponse);

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Deletes the user's account owning an IoT device", response = ResponseBase.class)
	@RequestMapping(value = "/{username}", produces = {"application/json"},	method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseBase> delete(@PathVariable String username) {
		ResponseEntity<ResponseBase> response;
		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + username));

		service.delete(username);
		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);
		response = ResponseEntity.status(HttpStatus.OK).body(responseBase);

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists all accounts of the integrated IoT devices", response = AccountDto.class, responseContainer="List")
	@RequestMapping(value = "", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<Iterable> listAllAccounts() {

		logger.debug(LoggingHelper.logInboundRequest("/accounts"));
		ResponseEntity<Iterable> response = list(null);
		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists the accounts of the integrated IoT devices", response = AccountDto.class, responseContainer="List")
	@RequestMapping(value = "/{username}", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<Iterable> listAccount(@PathVariable String username) {

		logger.debug(LoggingHelper.logInboundRequest("/accounts/" + username));
		ResponseEntity<Iterable> response = list(username);
		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	public ResponseEntity<Iterable> list(String username) {
		ResponseEntity<Iterable> response = null;

		List<AccountDto> accounts = service.list(username);
		if (accounts != null && !accounts.isEmpty()) {
			response = ResponseEntity.status(HttpStatus.OK).body(accounts);
		} else {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(accounts);
		}

		return  response;
	}
}
