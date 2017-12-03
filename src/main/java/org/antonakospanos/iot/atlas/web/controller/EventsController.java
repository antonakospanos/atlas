package org.antonakospanos.iot.atlas.web.controller;

import io.swagger.annotations.*;
import org.antonakospanos.iot.atlas.web.dto.HeartbeatFailureResponse;
import org.antonakospanos.iot.atlas.web.dto.HeartbeatRequest;
import org.antonakospanos.iot.atlas.web.dto.HeartbeatSuccessResponse;
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
public class EventsController {

    @ApiOperation(value = "Consumes state events published by IoT devices", response = HeartbeatSuccessResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "The event is created!", response = HeartbeatSuccessResponse.class),
        @ApiResponse(code = 400, message = "The request is invalid!", response = HeartbeatFailureResponse.class),
        @ApiResponse(code = 500, message = "server error", response = HeartbeatFailureResponse.class)})
    @RequestMapping(value = "/heartbeat",
        produces = {"application/json"},
        consumes = {"application/json"},
        method = RequestMethod.POST)
    public ResponseEntity<HeartbeatSuccessResponse> heartbeat(@ApiParam(value = "Inventory item") @Valid @RequestBody HeartbeatRequest heartbeat) {

        // TODO
        return new ResponseEntity<HeartbeatSuccessResponse>(HttpStatus.OK);
    }

}
