package org.antonakospanos.iot.atlas.web.controller.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.antonakospanos.iot.atlas.service.ActionService;
import org.antonakospanos.iot.atlas.support.LoggingHelper;
import org.antonakospanos.iot.atlas.web.controller.BaseAtlasController;
import org.antonakospanos.iot.atlas.web.dto.response.CreateResponse;
import org.antonakospanos.iot.atlas.web.dto.response.CreateResponseData;
import org.antonakospanos.iot.atlas.web.dto.response.ResponseBase;
import org.antonakospanos.iot.atlas.web.dto.actions.ActionDto;
import org.antonakospanos.iot.atlas.web.dto.actions.ActionRequest;
import org.antonakospanos.iot.atlas.web.enums.Result;
import org.antonakospanos.iot.atlas.web.validator.ActionsValidator;
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
@Api(value = "Actions API", tags = "actions", position = 1, description = "Action Management")
@RequestMapping(value = "/actions")
public class ActionsController extends BaseAtlasController {

	private final static Logger logger = LoggerFactory.getLogger(ActionsController.class);

	@Autowired
	ActionService service;

	@RequestMapping(value = "", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.POST)
	@ApiOperation(value = "Creates actions to be executed by the integrated IoT devices", response = CreateResponse.class)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The action is created!", response = CreateResponse.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<CreateResponse> create(UriComponentsBuilder uriBuilder, @Valid @RequestBody ActionRequest request) {
		ResponseEntity<CreateResponse> response;
		logger.debug(LoggingHelper.logInboundRequest(request));

		ActionsValidator.validateAction(request);

		CreateResponseData data = service.create(request);
		UriComponents uriComponents =	uriBuilder.path("/{id}").buildAndExpand(data.getId());
		CreateResponse createResponse = CreateResponse.Builder().build(Result.SUCCESS).data(data);
		response = ResponseEntity.created(uriComponents.toUri()).body(createResponse);

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Deletes the scheduled action for the integrated IoT device", response = CreateResponse.class)
	@RequestMapping(value = "", produces = {"application/json"},	method = RequestMethod.DELETE)
	public ResponseEntity<ResponseBase> delete(@PathVariable UUID actionId) {
		ResponseEntity<ResponseBase> response;
		logger.debug(LoggingHelper.logInboundRequest("/actions/" + actionId));

		service.delete(actionId);
		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);
		response = ResponseEntity.status(HttpStatus.CREATED).body(responseBase);

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists the scheduled actions for the integrated IoT devices", response = ActionDto.class, responseContainer="List")
	@RequestMapping(value = "", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<Iterable> list(@RequestParam (required=false) String username,
	                                     @RequestParam (required=false) String deviceId,
	                                     @RequestParam (required=false) String moduleId) {
		ResponseEntity<Iterable> response = null;
		logger.debug(LoggingHelper.logInboundRequest("/actions?username=" + username + "&deviceId=" + deviceId + "&moduleId=" + moduleId));

		List<ActionDto> actions = service.list(username, deviceId, moduleId);
		if (actions != null && !actions.isEmpty()) {
			response = ResponseEntity.status(HttpStatus.OK).body(actions);
		} else {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(actions);
		}

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}
}
