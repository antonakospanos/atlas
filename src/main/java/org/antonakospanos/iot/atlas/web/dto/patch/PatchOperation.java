package org.antonakospanos.iot.atlas.web.dto.patch;

public enum PatchOperation {

	ADD, REPLACE, REMOVE;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
