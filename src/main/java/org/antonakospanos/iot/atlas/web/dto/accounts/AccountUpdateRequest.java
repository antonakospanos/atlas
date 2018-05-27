package org.antonakospanos.iot.atlas.web.dto.accounts;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class AccountUpdateRequest {

	@ApiModelProperty(example = "2017-11-19T16:52:40.000 UTC")
	private String timestamp;

	@Valid
	@NotNull
	@ApiModelProperty(required = true)
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
		if (!(o instanceof AccountUpdateRequest)) return false;

		AccountUpdateRequest that = (AccountUpdateRequest) o;

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
