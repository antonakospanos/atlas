package org.antonakospanos.iot.atlas.web.dto.alerts;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import org.antonakospanos.iot.atlas.web.dto.actions.ConditionDto;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.lang.NonNull;

import javax.validation.Valid;

/**
 * AlertBaseDto
 */
@JsonPropertyOrder({"name", "condition"})
public class AlertBaseDto {

	@NonNull
	@ApiModelProperty(example = "Security alarm triggered!", required = true)
	@Valid
	private String name;

	@NonNull
	@ApiModelProperty(required = true)
	@Valid
	private ConditionDto condition;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ConditionDto getCondition() {
		return condition;
	}

	public void setCondition(ConditionDto condition) {
		this.condition = condition;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AlertBaseDto)) return false;

		AlertBaseDto that = (AlertBaseDto) o;

		return condition != null ? condition.equals(that.condition) : that.condition == null;
	}

	@Override
	public int hashCode() {
		return condition != null ? condition.hashCode() : 0;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
