package org.antonakospanos.iot.atlas.web.dto.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.UUID;

public class ActionRequest {

	@JsonProperty("timestamp")
	@ApiModelProperty(example = "2017-11-19T16:52:40.000 UTC")
	private String timestamp = null;

	@JsonProperty("accountId")
	@ApiModelProperty(example = "297dd70e-de40-4c82-8b75-f7183474d848")
	private UUID accountId = null;

	@JsonProperty("action")
	private ActionBaseDto action = null;

	@JsonProperty("alert")
	@ApiModelProperty(example = "true")
	private Boolean alert = false;

	public ActionRequest() {
	}

	public ActionRequest(String timestamp, UUID accountId, ActionBaseDto action, Boolean alert) {
		this.timestamp = timestamp;
		this.accountId = accountId;
		this.action = action;
		this.alert = alert;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public UUID getAccountId() {
		return accountId;
	}

	public void setAccountId(UUID accountId) {
		this.accountId = accountId;
	}

	public ActionBaseDto getAction() {
		return action;
	}

	public void setAction(ActionBaseDto action) {
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
		if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) return false;
		if (action != null ? !action.equals(that.action) : that.action != null) return false;
		return alert != null ? alert.equals(that.alert) : that.alert == null;
	}

	@Override
	public int hashCode() {
		int result = timestamp != null ? timestamp.hashCode() : 0;
		result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
		result = 31 * result + (action != null ? action.hashCode() : 0);
		result = 31 * result + (alert != null ? alert.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
