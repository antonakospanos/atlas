package org.antonakospanos.iot.atlas.web.dto.actions;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class ConditionDto implements Serializable {

	@NotNull
	List<ConjunctiveConditionDto> conjunctives;

	public List<ConjunctiveConditionDto> getConjunctives() {
		return conjunctives;
	}

	public void setConjunctives(List<ConjunctiveConditionDto> conjunctives) {
		this.conjunctives = conjunctives;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ConditionDto)) return false;

		ConditionDto that = (ConditionDto) o;

		return conjunctives.equals(that.conjunctives);
	}

	@Override
	public int hashCode() {
		return conjunctives.hashCode();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
