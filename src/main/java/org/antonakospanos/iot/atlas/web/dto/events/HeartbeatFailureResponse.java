package org.antonakospanos.iot.atlas.web.dto.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.antonakospanos.iot.atlas.web.dto.response.ResponseBase;
import org.antonakospanos.iot.atlas.web.enums.Result;

/**
 * HeartbeatFailureResponse
 */
@JsonPropertyOrder({ "result", "description", "data" })
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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

