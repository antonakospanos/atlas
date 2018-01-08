package org.antonakospanos.iot.atlas.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import org.antonakospanos.iot.atlas.dao.model.Action;
import org.antonakospanos.iot.atlas.dao.model.Alert;
import org.antonakospanos.iot.atlas.enums.ModuleState;
import org.antonakospanos.iot.atlas.web.dto.accounts.AccountDto;
import org.antonakospanos.iot.atlas.web.dto.alerts.AlertDto;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * ModuleActionDto
 */
@JsonPropertyOrder({"id", "state", "value"})
public class ModuleActionDto {

	@NotNull
	@ApiModelProperty(example = "thermometer_01", required = true)
	private String id;

	@JsonProperty("state")
	@ApiModelProperty(example = "1", allowableValues = "0,1,2,3,4", notes = "ENABLED(0), DISABLED(1), ARMED(2), DISARMED(3), ERROR(4)")
	private ModuleState state;

	@ApiModelProperty(example = "36")
	private String value;

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private AccountAlertDto accountAlert;

	public ModuleActionDto() {
	}

	public ModuleActionDto(String id, ModuleState state, String value) {
		this.id = id;
		this.state = state;
		this.value = value;
	}

	public ModuleActionDto(String id, ModuleState state, String value, Alert alert) {
		this(id, state, value);

		if (alert != null) {
			AlertDto alertDto = new AlertDto().fromEntity(alert);
			AccountDto accountDto = new AccountDto().fromEntity(alert.getAccount());
			AccountAlertDto accountAlertDto = new AccountAlertDto(alertDto, accountDto);
			this.accountAlert = accountAlertDto;
		}
	}

	public ModuleActionDto(String id, Action action) {
		this(id, action.getState(), action.getValue(), action.getCondition() != null ? action.getCondition().getAlert() : null);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ModuleState getState() {
		return state;
	}

	public void setState(ModuleState state) {
		this.state = state;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	public AccountAlertDto getAccountAlert() {
		return accountAlert;
	}

	public void setAccountAlert(AccountAlertDto accountAlertDto) {
		this.accountAlert = accountAlertDto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, state, value);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
