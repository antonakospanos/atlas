package org.antonakospanos.iot.atlas.web.dto.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import org.antonakospanos.iot.atlas.dao.model.Action;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.enums.ModuleState;
import org.antonakospanos.iot.atlas.web.dto.Dto;
import org.antonakospanos.iot.atlas.web.dto.ModuleActionDto;
import org.antonakospanos.iot.atlas.web.enums.Unit;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.UUID;

/**
 * AlertDto
 */
@JsonPropertyOrder({"id, execution", "recurring", "device", "condition"})
public class ActionDto extends ActionBaseDto implements Dto<Action> {

	@JsonProperty("id")
	@ApiModelProperty(example = "actionId")
	private UUID id;

	public ActionDto() {
	}

	public ActionDto(UUID id, ActionBaseDto actionBaseDto) {
		this(actionBaseDto);
		this.id = id;
	}

	public ActionDto(ActionBaseDto actionBaseDto) {
		super(actionBaseDto.getExecution(), actionBaseDto.getRecurring(), actionBaseDto.getDevice(), actionBaseDto.getCondition());
	}


	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public ActionDto fromEntity(Action action) {

		this.id = action.getExternalId();
		setExecution(action.getNextExecution());
		if (action.getPeriodInSecods() != null && action.getPeriodInSecods() != 0) {
			setRecurring(new RecurringActionDto(action.getPeriodInSecods(), Unit.SECONDS.toString()));
		}

		Module module = action.getModule();
		String deviceId = module.getDevice().getExternalId();
		ModuleActionDto moduleAction = new ModuleActionDto(module.getExternalId(), action.getState(), action.getValue());
		DeviceActionDto deviceAction = new DeviceActionDto(deviceId, moduleAction);
		setDevice(deviceAction);

		if (action.getCondition() != null) {
			setCondition(new ConditionDto().fromEntity(action.getCondition()));
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
		action.setNextExecution(this.getExecution());

		if (this.getRecurring() != null) {
			action.setPeriodInSecods(this.getRecurring().getPeriod());
		}
		if (this.getCondition() != null) {
			action.setCondition(this.getCondition().toEntity());
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
