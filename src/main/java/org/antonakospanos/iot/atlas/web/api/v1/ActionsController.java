package org.antonakospanos.iot.atlas.web.api.v1;

import io.swagger.annotations.*;
import org.antonakospanos.iot.atlas.service.ActionService;
import org.antonakospanos.iot.atlas.web.api.BaseAtlasController;
import org.antonakospanos.iot.atlas.web.dto.actions.ActionDto;
import org.antonakospanos.iot.atlas.web.dto.actions.ActionRequest;
import org.antonakospanos.iot.atlas.web.dto.response.CreateResponse;
import org.antonakospanos.iot.atlas.web.dto.response.CreateResponseData;
import org.antonakospanos.iot.atlas.web.dto.response.ResponseBase;
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
@RequestMapping(value = "/api/actions")
public class ActionsController extends BaseAtlasController {

	private final static Logger logger = LoggerFactory.getLogger(ActionsController.class);

	@Autowired
	ActionService service;

	@RequestMapping(value = "", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.POST)
	@ApiOperation(value = "Creates actions to be executed by the integrated IoT devices", response = CreateResponse.class)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The action is created!", response = CreateResponse.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<CreateResponse> create(UriComponentsBuilder uriBuilder, @Valid @RequestBody ActionRequest request) {
		ActionsValidator.validateAction(request);

		CreateResponseData data = service.create(request);
		UriComponents uriComponents =	uriBuilder.path("/actions/{id}").buildAndExpand(data.getId());
		CreateResponse createResponse = CreateResponse.Builder().build(Result.SUCCESS).data(data);

		return ResponseEntity.created(uriComponents.toUri()).body(createResponse);
	}

	@ApiOperation(value = "Deletes the scheduled action for the integrated IoT device", response = ResponseBase.class)
	@RequestMapping(value = "/{actionId}", produces = {"application/json"},	method = RequestMethod.DELETE)
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
	public ResponseEntity<ResponseBase> delete(@PathVariable UUID actionId, @RequestParam (required=false, defaultValue = "true") boolean deleteAlert) {
		service.delete(actionId, deleteAlert);
		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);

		return ResponseEntity.status(HttpStatus.OK).body(responseBase);
	}

	@ApiOperation(value = "Lists the scheduled actions for the integrated IoT devices", response = ActionDto.class, responseContainer="List")
	@RequestMapping(value = "", produces = {"application/json"},	method = RequestMethod.GET)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	public ResponseEntity<Iterable> list(@RequestParam (required=false) UUID accountId,
	                                     @RequestParam (required=false) String deviceId,
	                                     @RequestParam (required=false) String moduleId) {
		ResponseEntity<Iterable> response;

		List<ActionDto> actions = service.list(accountId, deviceId, moduleId);
		if (actions != null && !actions.isEmpty()) {
			response = ResponseEntity.status(HttpStatus.OK).body(actions);
		} else {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(actions);
		}

		return response;
	}
}
