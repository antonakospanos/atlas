package org.antonakospanos.iot.atlas.web.dto.alerts;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public class AlertRequest {

	@ApiModelProperty(example = "2017-11-19T16:52:40.000 UTC")
	private String timestamp;

	@NotNull
	@ApiModelProperty(example = "297dd70e-de40-4c82-8b75-f7183474d848", required = true)
	private UUID accountId;

	@NotNull
	@ApiModelProperty(required = true)
	@Valid
	private AlertBaseDto alert;

	public AlertRequest() {
	}

	public AlertRequest(String timestamp, UUID accountId, AlertBaseDto alert) {
		this.timestamp = timestamp;
		this.accountId = accountId;
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
		if (!accountId.equals(that.accountId)) return false;
		return alert.equals(that.alert);
	}

	@Override
	public int hashCode() {
		int result = timestamp.hashCode();
		result = 31 * result + accountId.hashCode();
		result = 31 * result + alert.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
