package org.antonakospanos.iot.atlas.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum ModuleState {

	ENABLED(0), DISABLED(1), ARMED(2), DISARMED(3), ERROR(4);

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

	@JsonValue
	public int toValue() {
		return this.number;
	}

	@JsonCreator
	public static ModuleState forValue(int number) {
		for (ModuleState state : ModuleState.values()) {
			if (state.getNumber() == number) {
				return state;
			}
		}
		return null;
	}
}
