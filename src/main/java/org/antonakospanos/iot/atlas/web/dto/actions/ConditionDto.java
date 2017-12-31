package org.antonakospanos.iot.atlas.web.dto.actions;

import io.swagger.annotations.ApiModelProperty;
import org.antonakospanos.iot.atlas.dao.model.Condition;
import org.antonakospanos.iot.atlas.dao.model.ConditionOrStatement;
import org.antonakospanos.iot.atlas.web.dto.Dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConditionDto implements Dto<Condition> {

	@NotNull
	@ApiModelProperty(required = true)
	@Valid
	private List<ConditionOrStatementDto> orLegs;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ConditionDto)) return false;

		ConditionDto that = (ConditionDto) o;

		return orLegs != null ? orLegs.equals(that.orLegs) : that.orLegs == null;
	}

	@Override
	public int hashCode() {
		return orLegs != null ? orLegs.hashCode() : 0;
	}

	public List<ConditionOrStatementDto> getOrLegs() {
		return orLegs;
	}

	public void setOrLegs(List<ConditionOrStatementDto> orLegs) {
		this.orLegs = orLegs;
	}


	@Override
	public ConditionDto fromEntity(Condition condition) {
		this.orLegs = condition.getConditionOrStatements()
				.stream()
				.map(conditionOrStatementDto -> new ConditionOrStatementDto().fromEntity(conditionOrStatementDto))
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

		Set<ConditionOrStatement> conditionOrStatements = this.getOrLegs()
				.stream()
				.map(conditionOrStatementDto -> {
					ConditionOrStatement conditionOrStatement = conditionOrStatementDto.toEntity();
					conditionOrStatement.setCondition(condition);

					return  conditionOrStatement;
				})
				.collect(Collectors.toSet());

		condition.setConditionOrStatements(conditionOrStatements);

		return condition;
	}
}
