package org.antonakospanos.iot.atlas.web.controller;

import io.swagger.annotations.*;
import org.antonakospanos.iot.atlas.service.EventsService;
import org.antonakospanos.iot.atlas.support.LoggingHelper;
import org.antonakospanos.iot.atlas.web.dto.*;
import org.antonakospanos.iot.atlas.web.enums.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(value = "Events API", tags = "events", position = 0 , description = "Consumes state events from IoT devices")
@RequestMapping(value = "/events")
public class EventsController extends BaseAtlasController {

    private final static Logger logger = LoggerFactory.getLogger(EventsController.class);

    @Autowired
    EventsService service;

    @ApiOperation(value = "Consumes state events published by IoT devices", response = HeartbeatSuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "The event is created!", response = HeartbeatSuccessResponse.class),
        @ApiResponse(code = 400, message = "The request is invalid!", response = HeartbeatFailureResponse.class),
        @ApiResponse(code = 500, message = "server error", response = HeartbeatFailureResponse.class)})
    @RequestMapping(value = "/heartbeat",
        produces = {"application/json"},
        consumes = {"application/json"},
        method = RequestMethod.POST)
    public ResponseEntity<ResponseBase> heartbeat(@ApiParam(value = "Inventory item") @Valid @RequestBody HeartbeatRequest heartbeat) {
        logger.debug(LoggingHelper.logInboundRequest(heartbeat));
        ResponseEntity<ResponseBase> heartbeatResponse;

        try {
            HeartbeatResponseData data = service.addEvent(heartbeat);

            HeartbeatSuccessResponse response = HeartbeatSuccessResponse.Builder().build(Result.SUCCESS).data(data);
            heartbeatResponse = ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error(e.getClass() + " Cause: " + e.getCause() + " Message: " + e.getMessage() + ". Heartbeat request: " + heartbeat, e);
            HeartbeatFailureResponse response = HeartbeatFailureResponse.Builder().build(Result.GENERIC_ERROR);
            heartbeatResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        logger.debug(LoggingHelper.logInboundResponse(heartbeatResponse));

        return heartbeatResponse;
    }
}
