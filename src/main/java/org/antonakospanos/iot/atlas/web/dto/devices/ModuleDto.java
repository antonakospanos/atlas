package org.antonakospanos.iot.atlas.web.dto.devices;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.enums.ModuleState;
import org.antonakospanos.iot.atlas.web.dto.Dto;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * ModuleDto
 */
@JsonPropertyOrder({ "id", "name", "type", "state", "value" })
public class ModuleDto implements Dto<Module> {

	@NotNull
	@ApiModelProperty(example = "thermometer_01", required = true)
	private String id;

	@ApiModelProperty(example = "living room thermometer")
	private String name;

	@NotNull
	@ApiModelProperty(example = "thermometer", required = true)
	private String type;

	@JsonProperty("state")
	@ApiModelProperty(example = "1", allowableValues = "0,1,2,3,4", notes = "ENABLED(0), DISABLED(1), ARMED(2), DISARMED(3), ERROR(4)")
	private ModuleState state;

	@ApiModelProperty(example = "36")
	private String value;

	public ModuleDto() {
	}

	public ModuleDto(String id, String name, String type, ModuleState state, String value) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.state = state;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ModuleState getState() {
		return state;
	}

	public void setState(ModuleState state) {
		this.state = state;
	}

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

		if (id != null ? !id.equals(moduleDto.id) : moduleDto.id != null) return false;
		if (name != null ? !name.equals(moduleDto.name) : moduleDto.name != null) return false;
		if (type != null ? !type.equals(moduleDto.type) : moduleDto.type != null) return false;
		if (state != moduleDto.state) return false;
		return value != null ? value.equals(moduleDto.value) : moduleDto.value == null;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, type, state, value);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public ModuleDto fromEntity(Module module) {
		this.id = module.getExternalId();
		this.name = module.getName();
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
		module.setExternalId(this.getId());
		module.setName(this.getName());
		module.setState(this.getState());
		module.setType(this.getType());
		module.setValue(this.getValue());

		return module;
	}
}

