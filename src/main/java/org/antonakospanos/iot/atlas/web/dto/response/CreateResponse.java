package org.antonakospanos.iot.atlas.web.dto.response;

import org.antonakospanos.iot.atlas.web.enums.Result;

public class CreateResponse extends Response {

	public static CreateResponse Builder() {
		return new CreateResponse();
	}

	public CreateResponse result(Result result) {
		setResult(result);
		return this;
	}

	public CreateResponse build(Result result) {
		setResult(result);
		setDescription(result.getDescription());
		return this;
	}

	public CreateResponse data(Object data) {
		setData(data);
		return this;
	}

	@Override
	public CreateResponseData getData() {
		return (CreateResponseData) super.getData();
	}

	public void setData(CreateResponseData data) {
		super.setData(data);
	}
}
