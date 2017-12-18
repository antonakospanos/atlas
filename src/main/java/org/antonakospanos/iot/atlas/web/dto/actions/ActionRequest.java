package org.antonakospanos.iot.atlas.web.dto.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ActionRequest {

	@JsonProperty("timestamp")
	private String timestamp = null;

	@JsonProperty("username")
	private String username = null;

	@JsonProperty("action")
	private ActionDto action = null;

	public ActionRequest() {
	}

	public ActionRequest(String timestamp, String username, ActionDto action) {
		this.timestamp = timestamp;
		this.username = username;
		this.action = action;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ActionRequest)) return false;

		ActionRequest that = (ActionRequest) o;

		if (!timestamp.equals(that.timestamp)) return false;
		if (!username.equals(that.username)) return false;
		return action.equals(that.action);
	}

	@Override
	public int hashCode() {
		int result = timestamp.hashCode();
		result = 31 * result + username.hashCode();
		result = 31 * result + action.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
