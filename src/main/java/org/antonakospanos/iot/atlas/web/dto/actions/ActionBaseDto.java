package org.antonakospanos.iot.atlas.web.dto.actions;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * ActionBaseDto
 */
@JsonPropertyOrder({ "execution", "recurring", "device", "condition" })
public class ActionBaseDto {

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS ZZZ")
	private ZonedDateTime execution;

	private RecurringActionDto recurring;

	@NotNull
	private DeviceActionDto device;

	private ConditionDto condition;

	public ZonedDateTime getExecution() {
		return execution;
	}

	public void setExecution(ZonedDateTime execution) {
		this.execution = execution;
	}

	public RecurringActionDto getRecurring() {
		return recurring;
	}

	public void setRecurring(RecurringActionDto recurring) {
		this.recurring = recurring;
	}

	public DeviceActionDto getDevice() {
		return device;
	}

	public void setDevice(DeviceActionDto device) {
		this.device = device;
	}

	public ConditionDto getCondition() {
		return condition;
	}

	public void setCondition(ConditionDto condition) {
		this.condition = condition;
	}

	public ActionBaseDto() {
	}

	public ActionBaseDto(@NotNull ZonedDateTime execution, RecurringActionDto recurring, @NotNull DeviceActionDto device, ConditionDto condition) {
		this.execution = execution;
		this.recurring = recurring;
		this.device = device;
		this.condition = condition;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ActionBaseDto)) return false;

		ActionBaseDto actionDto = (ActionBaseDto) o;

		if (!execution.equals(actionDto.execution)) return false;
		if (recurring != null ? !recurring.equals(actionDto.recurring) : actionDto.recurring != null) return false;
		if (!device.equals(actionDto.device)) return false;
		return condition != null ? condition.equals(actionDto.condition) : actionDto.condition == null;
	}

	@Override
	public int hashCode() {
		int result = execution.hashCode();
		result = 31 * result + (recurring != null ? recurring.hashCode() : 0);
		result = 31 * result + device.hashCode();
		result = 31 * result + (condition != null ? condition.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
