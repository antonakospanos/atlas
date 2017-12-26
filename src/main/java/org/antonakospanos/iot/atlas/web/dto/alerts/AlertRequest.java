package org.antonakospanos.iot.atlas.web.dto.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AlertRequest {

	@JsonProperty("timestamp")
	@ApiModelProperty(example = "2017-11-19T16:52:40.000 UTC")
	private String timestamp = null;

	@JsonProperty("username")
	@ApiModelProperty(example = "ckar")
	private String username = null;

	@JsonProperty("alert")
	private AlertBaseDto alert = null;

	public AlertRequest() {
	}

	public AlertRequest(String timestamp, String username, AlertBaseDto alert) {
		this.timestamp = timestamp;
		this.username = username;
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

	public AlertBaseDto getAlert() {
		return alert;
	}

	public void setAlert(AlertBaseDto alert) {
		this.alert = alert;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AlertRequest)) return false;

		AlertRequest that = (AlertRequest) o;

		if (!timestamp.equals(that.timestamp)) return false;
		if (!username.equals(that.username)) return false;
		return alert.equals(that.alert);
	}

	@Override
	public int hashCode() {
		int result = timestamp.hashCode();
		result = 31 * result + username.hashCode();
		result = 31 * result + alert.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
