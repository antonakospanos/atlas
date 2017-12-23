package org.antonakospanos.iot.atlas.web.controller.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.antonakospanos.iot.atlas.service.ActionService;
import org.antonakospanos.iot.atlas.support.LoggingHelper;
import org.antonakospanos.iot.atlas.web.controller.BaseAtlasController;
import org.antonakospanos.iot.atlas.web.dto.actions.ActionDto;
import org.antonakospanos.iot.atlas.web.dto.actions.ActionRequest;
import org.antonakospanos.iot.atlas.web.dto.actions.ActionResponse;
import org.antonakospanos.iot.atlas.web.dto.actions.ActionResponseData;
import org.antonakospanos.iot.atlas.web.enums.Result;
import org.antonakospanos.iot.atlas.web.validator.ActionsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value = "Actions API", tags = "actions", position = 1, description = "Action Management")
@RequestMapping(value = "/actions")
public class ActionsController extends BaseAtlasController {

	private final static Logger logger = LoggerFactory.getLogger(ActionsController.class);

	@Autowired
	ActionService service;

	@RequestMapping(
			produces = {"application/json"},
			consumes = {"application/json"},
			method = RequestMethod.POST)
	@ApiOperation(value = "Creates actions to be executed by the integrated IoT devices", response = ActionResponse.class)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The action is created!", response = ActionResponse.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<ActionResponse> add(@Valid @RequestBody ActionRequest request) {
		logger.debug(LoggingHelper.logInboundRequest(request));
		ResponseEntity<ActionResponse> response;

		ActionsValidator.validateAction(request);
		try {
			ActionResponseData data = service.add(request);

			ActionResponse actionResponse = ActionResponse.Builder().build(Result.SUCCESS).data(data);
			response = ResponseEntity.status(HttpStatus.CREATED).body(actionResponse);
		} catch (Exception e) {
			logger.error(e.getClass() + " Cause: " + e.getCause() + " Message: " + e.getMessage() + ". Action request: " + request, e);
			ActionResponse actionResponse = ActionResponse.Builder().build(Result.GENERIC_ERROR);
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(actionResponse);
		}

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Deletes the scheduled action for the integrated IoT device", response = ActionResponse.class)
	@RequestMapping(value = "", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<ActionResponse> delete(@PathVariable String actionId) {

		logger.debug(LoggingHelper.logInboundRequest("/actions/" + actionId));
		ResponseEntity<ActionResponse> response;

		try {
			service.delete(actionId);
			ActionResponse actionResponse = ActionResponse.Builder().build(Result.SUCCESS);
			response = ResponseEntity.status(HttpStatus.CREATED).body(actionResponse);
		} catch (Exception e) {
			logger.error(e.getClass() + " Cause: " + e.getCause() + " Message: " + e.getMessage() + ". Action request to be deleted: " + actionId, e);
			ActionResponse actionResponse = ActionResponse.Builder().build(Result.GENERIC_ERROR);
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(actionResponse);
		}

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists the scheduled actions for the integrated IoT devices", response = ActionDto.class, responseContainer="List")
	@RequestMapping(value = "", produces = {"application/json"},	method = RequestMethod.GET)
	public ResponseEntity<Iterable> list(@RequestParam (required=false) String username,
	                                     @RequestParam (required=false) String deviceId,
	                                     @RequestParam (required=false) String moduleId) {

		logger.debug(LoggingHelper.logInboundRequest("/actions?username=" + username + "&deviceId=" + deviceId + "&moduleId=" + moduleId));
		ResponseEntity<Iterable> response = null;
		try {
			List<ActionDto> actions = service.list(username, deviceId, moduleId);

			if (actions != null && !actions.isEmpty()) {
				response = ResponseEntity.status(HttpStatus.OK).body(actions);
			} else {
				response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(actions);
			}
		} catch (Exception e) {
			logger.error(e.getClass() + " Cause: " + e.getCause() + " Message: " + e.getMessage(), e);
		}

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}
}
