package org.antonakospanos.iot.atlas.web.dto.alerts;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import org.antonakospanos.iot.atlas.dao.model.Alert;
import org.antonakospanos.iot.atlas.dao.model.Condition;
import org.antonakospanos.iot.atlas.web.dto.Dto;
import org.antonakospanos.iot.atlas.web.dto.actions.ConditionDto;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.UUID;

/**
 * AlertDto
 */
@JsonPropertyOrder({"id, condition"})
public class AlertDto extends AlertBaseDto implements Dto<Alert> {

	@ApiModelProperty(example = "alertId")
	private UUID id;

	private ConditionDto condition;

	public UUID getId() {
		return id;
	}

	public ConditionDto getCondition() {
		return condition;
	}

	public void setCondition(ConditionDto condition) {
		this.condition = condition;
	}

	public AlertDto() {
	}

	public AlertDto(UUID id, ConditionDto condition) {
		this.id = id;
		setCondition(condition);
	}

	public AlertDto(AlertBaseDto alertBaseDto) {
		setCondition(alertBaseDto.getCondition());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AlertDto)) return false;

		AlertDto alertDto = (AlertDto) o;

		if (id != null ? !id.equals(alertDto.id) : alertDto.id != null) return false;
		return condition != null ? condition.equals(alertDto.condition) : alertDto.condition == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (condition != null ? condition.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public AlertDto fromEntity(Alert alert) {

		this.id = alert.getExternalId();
		if (alert.getCondition() != null) {
			this.condition = new ConditionDto().fromEntity(alert.getCondition());
		}

		return this;
	}

	@Override
	public Alert toEntity() {
		Alert alert = new Alert();

		return toEntity(alert);
	}

	@Override
	public Alert toEntity(Alert alert) {
		if (condition != null) {
			Condition condition = this.getCondition().toEntity();
			condition.setAlert(alert);
			alert.setCondition(condition);
		}
		// DAO: accountId

		return alert;
	}
}
