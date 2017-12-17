package org.antonakospanos.iot.atlas.web.dto.actions;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ConjunctiveConditionDto implements Serializable {

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
}
