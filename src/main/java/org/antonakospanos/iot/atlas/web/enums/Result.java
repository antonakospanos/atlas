package org.antonakospanos.iot.atlas.web.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Result {

	SUCCESS("SUCCESS"),
	BAD_REQUEST("BAD_REQUEST"),
	DUPLICATE_REQUEST("DUPLICATE_REQUEST"),
	TOO_MANY_REQUESTS("TOO_MANY_REQUESTS"),
	UNAUTHORIZED("UNAUTHORIZED"),
	GENERIC_ERROR("GENERIC_ERROR");

	private String value;

	Result(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static Result fromValue(String text) {
		for (Result b : Result.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
