package org.antonakospanos.iot.atlas.web.validator;

import org.antonakospanos.iot.atlas.web.dto.HeartbeatRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EventsValidator {

	public static void validateHeartBeat(HeartbeatRequest heartbeat) {

		List<String> moduleTypeList = heartbeat.getDevice().getModules()
				.stream()
				.map(module -> module.getType())
				.collect(Collectors.toList());

		Set<String> moduleTypeSet = new HashSet<String>(moduleTypeList);

		if (moduleTypeList.size() > moduleTypeSet.size()) {
			throw new IllegalArgumentException("Device's module types shall differ: " + moduleTypeList);
		}
	}
}