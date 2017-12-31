package org.antonakospanos.iot.atlas.web.dto.actions;

import io.swagger.annotations.ApiModelProperty;
import org.antonakospanos.iot.atlas.dao.model.ConditionAndStatement;
import org.antonakospanos.iot.atlas.dao.model.ConditionStatement;
import org.antonakospanos.iot.atlas.web.dto.Dto;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ConditionAndStatementDto implements Dto<ConditionAndStatement> {

	@NotNull
	@ApiModelProperty(required = true)
	@Valid
	private DeviceConditionDto device;

	private Boolean negated = false;

	public DeviceConditionDto getDevice() {
		return device;
	}

	public void setDevice(DeviceConditionDto device) {
		this.device = device;
	}

	public Boolean getNegated() {
		return negated;
	}

	public void setNegated(Boolean negated) {
		this.negated = negated;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ConditionAndStatementDto)) return false;

		ConditionAndStatementDto that = (ConditionAndStatementDto) o;

		return device.equals(that.device);
	}

	@Override
	public int hashCode() {
		return device.hashCode();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public ConditionAndStatementDto fromEntity(ConditionAndStatement conditionAndStatement) {

		ConditionStatement condition = conditionAndStatement.getConditionStatement();
		this.negated = condition.getNegated();

		ModuleConditionDto moduleConditionDto = new ModuleConditionDto();
		moduleConditionDto.setId(condition.getModule().getExternalId());
		moduleConditionDto.setMaxValue(condition.getMaxValue());
		moduleConditionDto.setMinValue(condition.getMinValue());
		moduleConditionDto.setValue(condition.getValue());
		moduleConditionDto.setState(condition.getState());

		String deviceId = condition.getModule().getDevice().getExternalId();
		this.device = new DeviceConditionDto(deviceId, moduleConditionDto);

		return this;
	}

	@Override
	public ConditionAndStatement toEntity() {
		ConditionAndStatement conditionAndStatement = new ConditionAndStatement();

		return toEntity(conditionAndStatement);
	}

	@Override
	public ConditionAndStatement toEntity(ConditionAndStatement conditionAndStatement) {

		ModuleConditionDto module = this.getDevice().getModule();

		ConditionStatement condition = new ConditionStatement();
		condition.setConditionAndStatement(conditionAndStatement);
		condition.setMaxValue(module.getMaxValue());
		condition.setMinValue(module.getMinValue());
		condition.setValue(module.getValue());
		condition.setState(module.getState());
		// Temp delegation:
		condition.setDeviceExternalId(this.getDevice().getId());
		condition.setModuleExternalId(module.getId());

		condition.setNegated(this.negated);
		conditionAndStatement.setConditionStatement(condition);

		return conditionAndStatement;
	}
}
