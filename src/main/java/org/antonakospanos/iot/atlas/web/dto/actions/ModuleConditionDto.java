package org.antonakospanos.iot.atlas.web.dto.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.antonakospanos.iot.atlas.enums.ModuleState;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ModuleConditionDto implements Serializable {

	@NotNull
	@ApiModelProperty(example = "thermometer_01", required = true)
	private String id;

	@JsonProperty("state")
	@ApiModelProperty(example = "1", allowableValues = "0,1,2,3,4", notes = "ENABLED(0), DISABLED(1), ARMED(2), DISARMED(3), ERROR(4)")
	private ModuleState state;

	@ApiModelProperty(example = "36")
	private String value;

	@ApiModelProperty(example = "32.5")
	private Double minValue;

	@ApiModelProperty(example = "38.5")
	private Double maxValue;

	public ModuleConditionDto() {
	}

	public ModuleConditionDto(@NotNull String id, ModuleState state, String value, Double minValue, Double maxValue) {
		this.id = id;
		this.state = state;
		this.value = value;
		this.minValue = minValue;
		this.maxValue = maxValue;
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

	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
