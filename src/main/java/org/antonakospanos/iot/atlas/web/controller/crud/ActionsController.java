package org.antonakospanos.iot.atlas.web.controller.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.antonakospanos.iot.atlas.service.ActionService;
import org.antonakospanos.iot.atlas.support.LoggingHelper;
import org.antonakospanos.iot.atlas.web.controller.BaseAtlasController;
import org.antonakospanos.iot.atlas.web.dto.ResponseBase;
import org.antonakospanos.iot.atlas.web.dto.actions.ActionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "Actions API", tags = "actions", position = 1, description = "Action Management")
@RequestMapping(value = "/actions")
public class ActionsController extends BaseAtlasController {

	private final static Logger logger = LoggerFactory.getLogger(ActionsController.class);

	@ApiOperation(value = "Not available yet", response = ResponseBase.class)
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> add() {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(RESPONSE);
	}

	@ApiOperation(value = "Not available yet", response = ResponseBase.class)
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Object> delete() {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(RESPONSE);
	}

	@Autowired
	ActionService service;

	@ApiOperation(value = "Lists the scheduled actions for the integrated the IoT devices", response = ActionDto.class, responseContainer="List")
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
