package org.antonakospanos.iot.atlas.web.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.antonakospanos.iot.atlas.web.dto.accounts.AccountDto;
import org.antonakospanos.iot.atlas.web.dto.alerts.AlertDto;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

/**
 * AccountAlertDto
 */
@JsonPropertyOrder({"alert", "account"})
public class AccountAlertDto {

	private AlertDto alert;

	private AccountDto account;

	public AccountAlertDto() {}

	public AccountAlertDto(AlertDto alert, AccountDto account) {
		this.alert = alert;
		this.account = account;
	}

	public AlertDto getAlert() {
		return alert;
	}

	public void setAlert(AlertDto alert) {
		this.alert = alert;
	}

	public AccountDto getAccount() {
		return account;
	}

	public void setAccount(AccountDto account) {
		this.account = account;
	}

	@Override
	public int hashCode() {
		return Objects.hash(alert, account);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
