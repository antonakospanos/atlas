package org.antonakospanos.iot.atlas.web.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class HeartbeatResponseData {

	private List<ModuleActionDto> actions;

	public List<ModuleActionDto> getActions() {
		return actions;
	}

	public void setActions(List<ModuleActionDto> actions) {
		this.actions = actions;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
