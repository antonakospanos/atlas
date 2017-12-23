package org.antonakospanos.iot.atlas.web.dto.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.antonakospanos.iot.atlas.dao.model.Action;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.enums.ModuleState;
import org.antonakospanos.iot.atlas.web.dto.Dto;
import org.antonakospanos.iot.atlas.web.dto.ModuleActionDto;
import org.antonakospanos.iot.atlas.web.enums.Unit;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * ActionDto
 */
@JsonPropertyOrder({"execution", "recurring", "device", "condition"})
public class ActionDto implements Dto<Action> {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private UUID id;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS ZZZ")
	private ZonedDateTime execution;

	private RecurringActionDto recurring;

	@NotNull
	private DeviceActionDto device;

	private ConditionDto condition;

	public UUID getId() {
		return id;
	}

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ActionDto)) return false;

		ActionDto actionDto = (ActionDto) o;

		if (id != null ? !id.equals(actionDto.id) : actionDto.id != null) return false;
		if (!execution.equals(actionDto.execution)) return false;
		if (recurring != null ? !recurring.equals(actionDto.recurring) : actionDto.recurring != null) return false;
		if (!device.equals(actionDto.device)) return false;
		return condition != null ? condition.equals(actionDto.condition) : actionDto.condition == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + execution.hashCode();
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

		this.id = action.getExternalId();
		this.execution = action.getNextExecution();
		if (action.getPeriodOfMinutes() != null && action.getPeriodOfMinutes() != 0) {
			this.recurring = new RecurringActionDto(action.getPeriodOfMinutes(), Unit.MINUTES.toString());
		}

		Module module = action.getModule();
		String deviceId = module.getDevice().getExternalId();
		ModuleActionDto moduleAction = new ModuleActionDto(module.getExternalId(), action.getState(), action.getValue());
		DeviceActionDto deviceAction = new DeviceActionDto(deviceId, moduleAction);
		this.device = deviceAction;

		if (action.getCondition() != null) {
			this.condition = new ConditionDto().fromEntity(action.getCondition());
		}

		return this;
	}

	@Override
	public Action toEntity() {
		Action action = new Action();

		return toEntity(action);
	}

	@Override
	public Action toEntity(Action action) {
		action.setNextExecution(this.execution);

		if (recurring != null) {
			action.setPeriodOfMinutes(recurring.getPeriod());
		}
		if (condition != null) {
			action.setCondition(this.condition.toEntity());
		}
		ModuleState state = this.getDevice().getModule().getState();
		if (state != null) {
			action.setState(state);
		}
		String value = this.getDevice().getModule().getValue();
		if (value != null) {
			action.setValue(value);
		}
		// DAO: accountId, moduleId

		return action;
	}
}
