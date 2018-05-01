package org.antonakospanos.iot.atlas.web.api.v1;

import io.swagger.annotations.*;
import org.antonakospanos.iot.atlas.service.AlertService;
import org.antonakospanos.iot.atlas.support.LoggingHelper;
import org.antonakospanos.iot.atlas.web.api.BaseAtlasController;
import org.antonakospanos.iot.atlas.web.dto.alerts.AlertDto;
import org.antonakospanos.iot.atlas.web.dto.alerts.AlertRequest;
import org.antonakospanos.iot.atlas.web.dto.response.CreateResponse;
import org.antonakospanos.iot.atlas.web.dto.response.CreateResponseData;
import org.antonakospanos.iot.atlas.web.dto.response.ResponseBase;
import org.antonakospanos.iot.atlas.web.enums.Result;
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
@Api(value = "Alerts API", tags = "alerts", position = 1, description = "Alert Management")
@RequestMapping(value = "/alerts")
public class AlertsController extends BaseAtlasController {

	private final static Logger logger = LoggerFactory.getLogger(AlertsController.class);

	@Autowired
	AlertService service;

	@RequestMapping(value = "", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.POST)
	@ApiOperation(value = "Creates alerts to be executed by the integrated IoT devices", response = CreateResponse.class)
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
			@ApiResponse(code = 201, message = "The alert is created!", response = CreateResponse.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<CreateResponse> create(UriComponentsBuilder uriBuilder, @Valid @RequestBody AlertRequest request) {
		ResponseEntity<CreateResponse> response;
		logger.debug(LoggingHelper.logInboundRequest(request));

		CreateResponseData data = service.create(request);
		UriComponents uriComponents =	uriBuilder.path("/alerts/{id}").buildAndExpand(data.getId());
		CreateResponse createResponse = CreateResponse.Builder().build(Result.SUCCESS).data(data);
		response = ResponseEntity.created(uriComponents.toUri()).body(createResponse);

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Deletes the scheduled alert for the integrated IoT device", response = ResponseBase.class)
	@RequestMapping(value = "/{alertId}", produces = {"application/json"},	method = RequestMethod.DELETE)
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
	public ResponseEntity<ResponseBase> delete(@PathVariable UUID alertId) {
		ResponseEntity<ResponseBase> response;
		logger.debug(LoggingHelper.logInboundRequest("/alerts/" + alertId));

		service.delete(alertId);
		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);
		response = ResponseEntity.status(HttpStatus.OK).body(responseBase);

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists the scheduled alerts for the integrated IoT devices", response = AlertDto.class, responseContainer="List")
	@RequestMapping(value = "", produces = {"application/json"},	method = RequestMethod.GET)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	public ResponseEntity<Iterable> list(@RequestParam (required=false) UUID accountId) {
		ResponseEntity<Iterable> response = null;
		logger.debug(LoggingHelper.logInboundRequest("/alerts?accountId=" + accountId ));

		List<AlertDto> alerts = service.list(accountId);
		if (alerts != null && !alerts.isEmpty()) {
			response = ResponseEntity.status(HttpStatus.OK).body(alerts);
		} else {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(alerts);
		}

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}
}
