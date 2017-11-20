package org.antonakospanos.iot.atlas.web.controller;

import io.swagger.annotations.*;
import io.swagger.model.Heartbeat;
import io.swagger.model.HeartbeatFailureResponse;
import io.swagger.model.HeartbeatSuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Api(value = "events", description = "the events API")
@Controller
public class EventsController {

    @ApiOperation(value = "Consumes state events published by IoT devices", notes = "The device sends the statistics of their enabled sensors", response = HeartbeatSuccessResponse.class, tags = {"events",})
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "The event is created!", response = HeartbeatSuccessResponse.class),
        @ApiResponse(code = 400, message = "The request is invalid!", response = HeartbeatFailureResponse.class),
        @ApiResponse(code = 500, message = "server error", response = HeartbeatFailureResponse.class)})
    @RequestMapping(value = "/events/heartbeat",
        produces = {"application/json"},
        consumes = {"application/json"},
        method = RequestMethod.POST)
    public ResponseEntity<HeartbeatSuccessResponse> heartbeat(@ApiParam(value = "Inventory item to add"  )  @Valid @RequestBody Heartbeat heartbeat) {

        // TODO
        return new ResponseEntity<HeartbeatSuccessResponse>(HttpStatus.OK);
    }

}
