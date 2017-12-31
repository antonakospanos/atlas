package org.antonakospanos.iot.atlas.web.dto.actions;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class DeviceConditionDto implements Serializable {

	@NotNull
	@ApiModelProperty(example = "deviceId", required = true)
	private String id;

	@NotNull
	@ApiModelProperty(required = true)
	@Valid
	private ModuleConditionDto module;

	public DeviceConditionDto() {
	}

	public DeviceConditionDto(@NotNull String id, @NotNull ModuleConditionDto module) {
		this.id = id;
		this.module = module;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ModuleConditionDto getModule() {
		return module;
	}

	public void setModule(ModuleConditionDto module) {
		this.module = module;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DeviceConditionDto)) return false;

		DeviceConditionDto that = (DeviceConditionDto) o;

		if (!id.equals(that.id)) return false;
		return module.equals(that.module);
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + module.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
