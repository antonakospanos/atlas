package org.antonakospanos.iot.atlas.web.dto.actions;

import org.antonakospanos.iot.atlas.web.enums.Unit;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class RecurringActionDto implements Serializable {

	@NotNull
	private String period;

	private String unit = Unit.MINUTES.toString();

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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
