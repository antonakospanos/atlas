package org.antonakospanos.iot.atlas.enums;

import java.util.HashMap;
import java.util.Map;

public enum ModuleState {

	ENABLED(0), DISABLED(1), ERROR(2);

	private static Map<Integer, ModuleState> map = new HashMap<>();
	static {
		for (ModuleState state : ModuleState.values()) {
			map.put(state.getNumber(), state);
		}
	}

	int number;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	ModuleState(int number) {
		this.number = number;
	}

	public static ModuleState lookup(Integer number) {
		return map.get(number);
	}
}
