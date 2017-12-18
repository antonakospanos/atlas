package org.antonakospanos.iot.atlas.web.dto.actions;

import org.antonakospanos.iot.atlas.dao.model.ConditionConjunctive;
import org.antonakospanos.iot.atlas.web.dto.Dto;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

public class ConjunctiveConditionDto implements Dto<ConditionConjunctive> {

	@NotNull
	DeviceConditionDto device;

	public DeviceConditionDto getDevice() {
		return device;
	}

	public void setDevice(DeviceConditionDto device) {
		this.device = device;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ConjunctiveConditionDto)) return false;

		ConjunctiveConditionDto that = (ConjunctiveConditionDto) o;

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
	public ConjunctiveConditionDto fromEntity(ConditionConjunctive condition) {

		String deviceId = condition.getModule().getDevice().getExternalId();

		ModuleConditionDto moduleConditionDto = new ModuleConditionDto();
		moduleConditionDto.setId(condition.getModule().getExternalId());
		moduleConditionDto.setMaxValue(condition.getMaxValue());
		moduleConditionDto.setMinValue(condition.getMinValue());
		moduleConditionDto.setValue(condition.getValue());
		moduleConditionDto.setState(condition.getState());

		this.setDevice(new DeviceConditionDto(deviceId, moduleConditionDto));

		return this;
	}

	@Override
	public ConditionConjunctive toEntity() {
		ConditionConjunctive conditionConjunctive = new ConditionConjunctive();

		return toEntity(conditionConjunctive);
	}

	@Override
	public ConditionConjunctive toEntity(ConditionConjunctive condition) {

		ModuleConditionDto module = this.getDevice().getModule();
		condition.setMaxValue(module.getMaxValue());
		condition.setMinValue(module.getMinValue());
		condition.setValue(module.getValue());
		condition.setState(module.getState());
		// DAO: condition.setModule();
		// DAO: condition.setCondition();

		return condition;
	}
}
