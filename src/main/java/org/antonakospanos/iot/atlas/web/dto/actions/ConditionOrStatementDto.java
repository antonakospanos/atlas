package org.antonakospanos.iot.atlas.web.dto.actions;

import org.antonakospanos.iot.atlas.dao.model.ConditionAndStatement;
import org.antonakospanos.iot.atlas.dao.model.ConditionOrStatement;
import org.antonakospanos.iot.atlas.web.dto.Dto;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConditionOrStatementDto implements Dto<ConditionOrStatement> {

	@NotNull
	List<ConditionAndStatementDto> andLegs;

	public List<ConditionAndStatementDto> getAndLegs() {
		return andLegs;
	}

	public void setAndLegs(List<ConditionAndStatementDto> andLegs) {
		this.andLegs = andLegs;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ConditionOrStatementDto)) return false;

		ConditionOrStatementDto that = (ConditionOrStatementDto) o;

		return andLegs.equals(that.andLegs);
	}

	@Override
	public int hashCode() {
		return andLegs.hashCode();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public ConditionOrStatementDto fromEntity(ConditionOrStatement conditionOrStatement) {

		this.andLegs = conditionOrStatement.getConditionAndStatements()
				.stream()
				.map(conditionAndStatement -> new ConditionAndStatementDto().fromEntity(conditionAndStatement))
				.collect(Collectors.toList());

		return this;
	}

	@Override
	public ConditionOrStatement toEntity() {
		ConditionOrStatement conditionOrStatement = new ConditionOrStatement();

		return toEntity(conditionOrStatement);
	}

	@Override
	public ConditionOrStatement toEntity(ConditionOrStatement conditionOrStatement) {

		Set<ConditionAndStatement> conditions = this.getAndLegs()
				.stream()
				.map(conditionDto -> conditionDto.toEntity())
				.collect(Collectors.toSet());

		conditionOrStatement.setConditionAndStatements(conditions);

		return conditionOrStatement;
	}
}
