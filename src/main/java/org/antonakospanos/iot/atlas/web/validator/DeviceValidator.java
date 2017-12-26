package org.antonakospanos.iot.atlas.web.validator;

import org.antonakospanos.iot.atlas.web.dto.devices.DeviceBaseDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DeviceValidator {

	public static void validateDeviceRequest(DeviceBaseDto deviceBaseDto) {

		List<String> moduleList = deviceBaseDto.getModules()
				.stream()
				.map(module -> module.getId())
				.collect(Collectors.toList());

		Set<String> moduleSet = new HashSet<String>(moduleList);

		if (moduleList.size() > moduleSet.size()) {
			throw new IllegalArgumentException("Device's Module IDs shall differ: " + moduleList);
		}
	}
}