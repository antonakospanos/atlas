package org.antonakospanos.iot.atlas.web.dto.actions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.antonakospanos.iot.atlas.dao.model.Action;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.web.dto.Dto;
import org.antonakospanos.iot.atlas.web.dto.ModuleActionDto;
import org.antonakospanos.iot.atlas.web.enums.Unit;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * ActionDto
 */
@JsonPropertyOrder({"date", "recurring", "device", "condition"})
public class ActionDto implements Dto<Action> {

	@NotNull
	private ZonedDateTime date;

	private RecurringActionDto recurring;

	@NotNull
	private DeviceActionDto device;

	private ConditionDto condition;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	public ZonedDateTime getDate() {
		return date;
	}

	public void setDate(ZonedDateTime date) {
		this.date = date;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ActionDto)) return false;

		ActionDto actionDto = (ActionDto) o;

		if (!date.equals(actionDto.date)) return false;
		if (recurring != null ? !recurring.equals(actionDto.recurring) : actionDto.recurring != null) return false;
		if (!device.equals(actionDto.device)) return false;
		return condition != null ? condition.equals(actionDto.condition) : actionDto.condition == null;
	}

	@Override
	public int hashCode() {
		int result = date.hashCode();
		result = 31 * result + (recurring != null ? recurring.hashCode() : 0);
		result = 31 * result + device.hashCode();
		result = 31 * result + (condition != null ? condition.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public ActionDto fromEntity(Action action) {

		this.date = action.getNextExecution();
		if (action.getPeriodOfMinutes() != null && action.getPeriodOfMinutes() != 0) {
			this.recurring = new RecurringActionDto(action.getPeriodOfMinutes(), Unit.MINUTES.toString());
		}

		Module module = action.getModule();
		String deviceId = module.getDevice().getExternalId();
		ModuleActionDto moduleAction = new ModuleActionDto(module.getExternalId(), action.getState(), action.getValue());
		DeviceActionDto deviceAction = new DeviceActionDto(deviceId, moduleAction);
		this.device = deviceAction;
		this.condition = new ConditionDto().fromEntity(action.getCondition());

		return this;
	}

	@Override
	public Action toEntity() {
		return null;
	}

	@Override
	public Action toEntity(Action entity) {
		return null;
	}
}
