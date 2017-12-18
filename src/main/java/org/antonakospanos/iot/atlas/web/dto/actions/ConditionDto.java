package org.antonakospanos.iot.atlas.web.dto.actions;

import org.antonakospanos.iot.atlas.dao.model.Condition;
import org.antonakospanos.iot.atlas.dao.model.ConditionConjunctive;
import org.antonakospanos.iot.atlas.web.dto.Dto;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class ConditionDto implements Dto<Condition> {

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

	@Override
	public ConditionDto fromEntity(Condition condition) {

		this.conjunctives = condition.getConditionConjunctives()
				.stream()
				.map(conditionConjunctive -> new ConjunctiveConditionDto().fromEntity(conditionConjunctive))
				.collect(Collectors.toList());

		return this;
	}

	@Override
	public Condition toEntity() {
		Condition condition = new Condition();

		return toEntity(condition);
	}

	@Override
	public Condition toEntity(Condition condition) {

		List<ConditionConjunctive> conditions = this.getConjunctives()
				.stream()
				.map(conditionDto -> conditionDto.toEntity())
				.collect(Collectors.toList());

		condition.setConditionConjunctives(conditions);

		return condition;
	}
}
