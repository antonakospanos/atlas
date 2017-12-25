package org.antonakospanos.iot.atlas.web.dto.actions;

import io.swagger.annotations.ApiModelProperty;
import org.antonakospanos.iot.atlas.web.enums.Unit;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class RecurringActionDto implements Serializable {

	@NotNull
	private Long period;

	@ApiModelProperty(example = "seconds")
	private String unit = Unit.SECONDS.toString();

	public RecurringActionDto() {
	}

	public RecurringActionDto(@NotNull Long period, String unit) {
		this.period = period;
		this.unit = unit;
	}

	public Long getPeriod() {
		return period;
	}

	public void setPeriod(Long period) {
		this.period = period;
	}

	public String getUnit() {
		return unit;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RecurringActionDto)) return false;

		RecurringActionDto that = (RecurringActionDto) o;

		if (!period.equals(that.period)) return false;
		return unit != null ? unit.equals(that.unit) : that.unit == null;
	}

	@Override
	public int hashCode() {
		int result = period.hashCode();
		result = 31 * result + (unit != null ? unit.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
