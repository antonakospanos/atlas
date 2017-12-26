package org.antonakospanos.iot.atlas.web.validator;

import org.antonakospanos.iot.atlas.web.dto.events.HeartbeatRequest;

public class EventsValidator {

	public static void validateHeartBeat(HeartbeatRequest heartbeat) {
		DeviceValidator.validateDeviceRequest(heartbeat.getDevice());
	}
}