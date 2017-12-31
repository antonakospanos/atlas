package org.antonakospanos.iot.atlas.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import org.antonakospanos.iot.atlas.enums.ModuleState;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * ModuleActionDto
 */
@JsonPropertyOrder({"id", "state", "value"})
public class ModuleActionDto {

	@NotNull
	@ApiModelProperty(example = "thermometer_01", required = true)
	private String id;

	@JsonProperty("state")
	@ApiModelProperty(example = "1", allowableValues = "0,1,2,3,4", notes = "ENABLED(0), DISABLED(1), ARMED(2), DISARMED(3), ERROR(4)")
	private ModuleState state;

	@ApiModelProperty(example = "36")
	private String value;

	public ModuleActionDto() {
	}

	public ModuleActionDto(String id, ModuleState state, String value) {
		this.id = id;
		this.state = state;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	public int hashCode() {
		return Objects.hash(id, state, value);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
