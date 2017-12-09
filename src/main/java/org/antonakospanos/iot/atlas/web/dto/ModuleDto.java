package org.antonakospanos.iot.atlas.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.enums.ModuleState;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * ModuleDto
 */
@JsonPropertyOrder({ "type", "state", "value" })
public class ModuleDto implements Dto<Module, Integer> {

	@JsonProperty("type")
	private String type = null;

	@JsonProperty("state")
	private ModuleState state = null;

	@JsonProperty("value")
	private String value = null;

	public ModuleDto() {
	}

	public ModuleDto(String type, ModuleState state, String value) {
		this.type = type;
		this.state = state;
		this.value = value;
	}

	@ApiModelProperty(example = "thermometer", required = true)
	@NotNull
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ApiModelProperty(example = "1")
	public ModuleState getState() {
		return state;
	}

	public void setState(ModuleState state) {
		this.state = state;
	}

	@ApiModelProperty(example = "36")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ModuleDto moduleDto = (ModuleDto) o;

		if (type != null ? !type.equals(moduleDto.type) : moduleDto.type != null) return false;
		if (state != moduleDto.state) return false;
		return value != null ? value.equals(moduleDto.value) : moduleDto.value == null;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, state, value);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public ModuleDto fromEntity(Module module) {
		this.type = module.getType();
		this.state = module.getState();
		this.value = module.getValue();

		return this;
	}

	@Override
	public Module toEntity() {
		Module module = new Module();

		return toEntity(module);
	}

	@Override
	public Module toEntity(Module module) {
		module.setState(this.getState());
		module.setType(this.getType());
		module.setValue(this.getValue());

		return module;
	}
}

