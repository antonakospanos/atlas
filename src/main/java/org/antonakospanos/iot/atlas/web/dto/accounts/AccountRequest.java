package org.antonakospanos.iot.atlas.web.dto.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AccountRequest {

	@JsonProperty("timestamp")
	private String timestamp = null;

	private AccountDto accountDto;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public AccountDto getAccountDto() {
		return accountDto;
	}

	public void setAccountDto(AccountDto accountDto) {
		this.accountDto = accountDto;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AccountRequest)) return false;

		AccountRequest that = (AccountRequest) o;

		if (!timestamp.equals(that.timestamp)) return false;
		return accountDto.equals(that.accountDto);
	}

	@Override
	public int hashCode() {
		int result = timestamp.hashCode();
		result = 31 * result + accountDto.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
