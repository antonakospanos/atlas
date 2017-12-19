package org.antonakospanos.iot.atlas.web.controller.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.antonakospanos.iot.atlas.service.AccountService;
import org.antonakospanos.iot.atlas.support.LoggingHelper;
import org.antonakospanos.iot.atlas.web.controller.BaseAtlasController;
import org.antonakospanos.iot.atlas.web.dto.ResponseBase;
import org.antonakospanos.iot.atlas.web.dto.accounts.AccountDto;
import org.antonakospanos.iot.atlas.web.dto.actions.ActionRequest;
import org.antonakospanos.iot.atlas.web.dto.actions.ActionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value = "Accounts API", tags = "accounts", position = 2, description = "Account Management")
@RequestMapping(value = "/accounts")
public class AccountController extends BaseAtlasController {

	private final static Logger logger = LoggerFactory.getLogger(AccountController.class);

	@ApiOperation(value = "Not available yet", response = ResponseBase.class)
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ActionResponse> add(@ApiParam(value = "Scheduled action") @Valid @RequestBody ActionRequest request) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
	}

	@ApiOperation(value = "Not available yet", response = ResponseBase.class)
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Object> delete() {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(RESPONSE);
	}

	@Autowired
	AccountService service;

	@ApiOperation(value = "Lists all accounts of the integrated IoT devices", response = AccountDto.class, responseContainer="List")
	@RequestMapping(value = "", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<Iterable> listAll() {
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
		try {
			List<AccountDto> accounts = service.list(username);

			if (accounts != null && !accounts.isEmpty()) {
				response = ResponseEntity.status(HttpStatus.OK).body(accounts);
			} else {
				response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(accounts);
			}
		} catch (Exception e) {
			logger.error(e.getClass() + " Cause: " + e.getCause() + " Message: " + e.getMessage(), e);
		}

		return  response;
	}
}
