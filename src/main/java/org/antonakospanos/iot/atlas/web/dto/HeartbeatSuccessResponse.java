package org.antonakospanos.iot.atlas.web.dto;

import org.antonakospanos.iot.atlas.web.enums.Result;

/**
 * HeartbeatSuccessResponse
 */
public class HeartbeatSuccessResponse extends ResponseBase {

	public static HeartbeatSuccessResponse Builder() {
		return new HeartbeatSuccessResponse();
	}

	private HeartbeatResponseData data;

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
		return data;
	}

	public void setData(HeartbeatResponseData data) {
		this.data = data;
	}
}

