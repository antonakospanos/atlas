package org.antonakospanos.iot.atlas.web.dto;

import java.util.List;

public class HeartbeatResponseData {

	private List<ModuleDto> actions;

	public List<ModuleDto> getActions() {
		return actions;
	}

	public void setActions(List<ModuleDto> actions) {
		this.actions = actions;
	}
}
