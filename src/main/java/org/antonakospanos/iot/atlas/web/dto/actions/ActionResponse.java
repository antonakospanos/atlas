package org.antonakospanos.iot.atlas.web.dto.actions;

import org.antonakospanos.iot.atlas.web.dto.ResponseBase;
import org.antonakospanos.iot.atlas.web.enums.Result;

public class ActionResponse extends ResponseBase {

	public static ActionResponse Builder() {
		return new ActionResponse();
	}

	public ActionResponse result(Result result) {
		setResult(result);
		return this;
	}

	public ActionResponse build(Result result) {
		setResult(result);
		setDescription(result.getDescription());
		return this;
	}

	public ActionResponse data(Object data) {
		setData(data);
		return this;
	}

	@Override
	public ActionResponseData getData() {
		return (ActionResponseData) super.getData();
	}

	public void setData(ActionResponseData data) {
		super.setData(data);
	}
}
