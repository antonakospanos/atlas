package org.antonakospanos.iot.atlas.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ModuleDto implements Dto<Module, Integer> {

	@JsonProperty("type")
	private String type = null;

	@JsonProperty("state")
	private Integer state = null;

	@JsonProperty("value")
	private String value = null;

	public ModuleDto() {
	}

	public ModuleDto(String type, Integer state, String value) {
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
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
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
	public int hashCode() {
		return Objects.hash(type, state, value);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public ModuleDto fromEntity(Module module) {
		this.type = module.getType();
		this.state = ModuleState.valueOf(module.getState()).getNumber();
		this.value = module.getValue();

		return this;
	}

	@Override
	public Module toEntity() {
		Module module = new Module();

		module.setState(ModuleState.lookup(this.getState()).name());
		module.setType(this.getType());
		module.setValue(this.getValue());

		return module;
	}
}

