package org.antonakospanos.iot.atlas.web.validator;

import org.antonakospanos.iot.atlas.web.dto.events.HeartbeatRequest;

public class DeviceValidator {

	public static void validateDeviceUpdate(String deviceId, HeartbeatRequest request) {

		if (!deviceId.equals(request.getDevice().getId())) {
			throw new IllegalArgumentException("Device '" + deviceId + "' does not exist!");
		}

		EventsValidator.validateHeartBeat(request);
	}
}