package org.antonakospanos.iot.atlas.web.api.v1;

import io.swagger.annotations.*;
import org.antonakospanos.iot.atlas.service.EventsService;
import org.antonakospanos.iot.atlas.web.api.BaseAtlasController;
import org.antonakospanos.iot.atlas.web.dto.events.HeartbeatFailureResponse;
import org.antonakospanos.iot.atlas.web.dto.events.HeartbeatRequest;
import org.antonakospanos.iot.atlas.web.dto.events.HeartbeatResponseData;
import org.antonakospanos.iot.atlas.web.dto.events.HeartbeatSuccessResponse;
import org.antonakospanos.iot.atlas.web.dto.response.ResponseBase;
import org.antonakospanos.iot.atlas.web.enums.Result;
import org.antonakospanos.iot.atlas.web.validator.EventsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Deprecated
@Api(value = "Events API", tags = "events", position = 0 , description = "Event Management of integrated IoT devices")
@RequestMapping(value = "/api/events")
public class EventsController extends BaseAtlasController {

    private final static Logger logger = LoggerFactory.getLogger(EventsController.class);

    @Autowired
    EventsService service;

    /**
     * Backwards compatibility of PUT /devices/{deviceId} API
     *
     * @param heartbeat The device's heartbeat event
     * @return The actions to be executed by the device
     * @deprecated Replaced by PUT /devices/{deviceId} API
     */
    @Deprecated
    @ApiOperation(value = "Consumes state events that are published by IoT devices", response = HeartbeatSuccessResponse.class,
            notes = "Backwards compatibility of PUT /devices/{deviceId} API")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "The event is created!", response = HeartbeatSuccessResponse.class),
        @ApiResponse(code = 400, message = "The request is invalid!"),
        @ApiResponse(code = 500, message = "Server Error")})
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/heartbeat",
        produces = {"application/json"},
        consumes = {"application/json"},
        method = RequestMethod.POST)
    public ResponseEntity<ResponseBase> heartbeat(@ApiParam(value = "Inventory item") @Valid @RequestBody HeartbeatRequest heartbeat) {
        ResponseEntity<ResponseBase> response;
        EventsValidator.validateHeartBeat(heartbeat);
        try {
            HeartbeatResponseData data = service.create(heartbeat);

            HeartbeatSuccessResponse heartbeatSuccessResponse = HeartbeatSuccessResponse.Builder().build(Result.SUCCESS).data(data);
            response = ResponseEntity.status(HttpStatus.CREATED).body(heartbeatSuccessResponse);
        } catch (Exception e) {
            logger.error(e.getClass() + " Cause: " + e.getCause() + " Message: " + e.getMessage() + ". Heartbeat request: " + heartbeat, e);
            HeartbeatFailureResponse heartbeatFailureResponse = HeartbeatFailureResponse.Builder().build(Result.GENERIC_ERROR);
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(heartbeatFailureResponse);
        }

        return response;
    }
}
