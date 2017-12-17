package org.antonakospanos.iot.atlas.web.dto.actions;

import org.antonakospanos.iot.atlas.web.dto.ModuleActionDto;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class DeviceActionDto implements Serializable {

	@NotNull
	private String id;

	@NotNull
	private ModuleActionDto module;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ModuleActionDto getModule() {
		return module;
	}

	public void setModule(ModuleActionDto module) {
		this.module = module;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DeviceActionDto)) return false;

		DeviceActionDto that = (DeviceActionDto) o;

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
