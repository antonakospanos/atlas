package org.antonakospanos.iot.atlas.web.controller;

import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Actions API", tags = "actions", position = 1, description = "Triggers actions to IoT devices")
@RequestMapping(value = "/actions")
public class ActionsController extends BaseAtlasController {

	private final static String RESPONSE = "Not available yet!";

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> add() {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(RESPONSE);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Object> update() {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(RESPONSE);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Object> list() {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(RESPONSE);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Object> delete() {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(RESPONSE);
	}
}
