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
@JsonPropertyOrder({"id", "name", "condition"})
public class AlertDto extends AlertBaseDto implements Dto<Alert> {

	@ApiModelProperty(example = "alertId")
	private UUID id;

	public UUID getId() {
		return id;
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
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public AlertDto fromEntity(Alert alert) {

		this.id = alert.getExternalId();
		setName(alert.getName());
		if (alert.getCondition() != null) {
			this.setCondition(new ConditionDto().fromEntity(alert.getCondition()));
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
		alert.setName(this.getName());

		if (this.getCondition() != null) {
			Condition condition = this.getCondition().toEntity();
			condition.setAlert(alert);
			alert.setCondition(condition);
		}
		// DAO: accountId

		return alert;
	}
}
