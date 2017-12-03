package org.antonakospanos.iot.atlas.web.dto;

import org.antonakospanos.iot.atlas.web.enums.Result;

/**
 * HeartbeatFailureResponse
 */
public class HeartbeatFailureResponse extends ResponseBase {

	public static HeartbeatFailureResponse Builder() {
		return new HeartbeatFailureResponse();
	}

	public HeartbeatFailureResponse build(Result result) {
		super.build(result);
		return this;
	}

	public HeartbeatFailureResponse result(Result result) {
		super.setResult(result);
		return this;
	}

	public HeartbeatFailureResponse description(String description) {
		super.setDescription(description);
		return this;
	}
}

