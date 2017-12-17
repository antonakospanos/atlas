package org.antonakospanos.iot.atlas.web.dto.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.antonakospanos.iot.atlas.web.dto.ResponseBase;
import org.antonakospanos.iot.atlas.web.enums.Result;

/**
 * HeartbeatSuccessResponse
 */
@JsonPropertyOrder({ "result", "description", "data" })
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HeartbeatSuccessResponse extends ResponseBase {

	public static HeartbeatSuccessResponse Builder() {
		return new HeartbeatSuccessResponse();
	}

	public HeartbeatSuccessResponse build(Result result) {
		super.build(result);
		return this;
	}

	public HeartbeatSuccessResponse result(Result result) {
		super.setResult(result);
		return this;
	}

	public HeartbeatSuccessResponse description(String description) {
		super.setDescription(description);
		return this;
	}

	public HeartbeatSuccessResponse data(HeartbeatResponseData data) {
		setData(data);
		return this;
	}

	@Override
	public HeartbeatResponseData getData() {
		return (HeartbeatResponseData) super.getData();
	}

	public void setData(HeartbeatResponseData data) {
		super.setData(data);
	}
}

