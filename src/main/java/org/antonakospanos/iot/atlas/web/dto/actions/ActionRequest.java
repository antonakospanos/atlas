package org.antonakospanos.iot.atlas.web.dto.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ActionRequest {

	@JsonProperty("timestamp")
	@ApiModelProperty(example = "2017-11-19T16:52:40.000 UTC")
	private String timestamp = null;

	@JsonProperty("username")
	@ApiModelProperty(example = "ckar")
	private String username = null;

	@JsonProperty("action")
	private ActionDto action = null;

	@JsonProperty("alert")
	@ApiModelProperty(example = "true")
	private Boolean alert = false;

	public ActionRequest() {
	}

	public ActionRequest(String timestamp, String username, ActionDto action, Boolean alert) {
		this.timestamp = timestamp;
		this.username = username;
		this.action = action;
		this.alert = alert;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ActionDto getAction() {
		return action;
	}

	public void setAction(ActionDto action) {
		this.action = action;
	}

	public Boolean getAlert() {
		return alert;
	}

	public void setAlert(Boolean alert) {
		this.alert = alert;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ActionRequest)) return false;

		ActionRequest that = (ActionRequest) o;

		if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
		if (username != null ? !username.equals(that.username) : that.username != null) return false;
		if (action != null ? !action.equals(that.action) : that.action != null) return false;
		return alert != null ? alert.equals(that.alert) : that.alert == null;
	}

	@Override
	public int hashCode() {
		int result = timestamp != null ? timestamp.hashCode() : 0;
		result = 31 * result + (username != null ? username.hashCode() : 0);
		result = 31 * result + (action != null ? action.hashCode() : 0);
		result = 31 * result + (alert != null ? alert.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
