package org.antonakospanos.iot.atlas.web.enums;

public enum Unit {

	HOURS, MINUTES, SECONDS;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
