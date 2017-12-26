package org.antonakospanos.iot.atlas.web.dto.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AccountRequest {

	@JsonProperty("timestamp")
	@ApiModelProperty(example = "2017-11-19T16:52:40.000 UTC")
	private String timestamp = null;

	private AccountDto account;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public AccountDto getAccount() {
		return account;
	}

	public void setAccount(AccountDto account) {
		this.account = account;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AccountRequest)) return false;

		AccountRequest that = (AccountRequest) o;

		if (!timestamp.equals(that.timestamp)) return false;
		return account.equals(that.account);
	}

	@Override
	public int hashCode() {
		int result = timestamp.hashCode();
		result = 31 * result + account.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
