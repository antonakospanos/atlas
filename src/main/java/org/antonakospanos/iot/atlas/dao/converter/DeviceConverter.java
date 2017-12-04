package org.antonakospanos.iot.atlas.dao.converter;

import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.web.dto.DeviceDto;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class DeviceConverter {

	public void updateDevice(DeviceDto deviceDto, Device device) {

		device.setExternalId(deviceDto.getId());
		device.setVersion(deviceDto.getVersion());
		device.setLastContact(ZonedDateTime.now());

		deviceDto.getModules()
				.stream()
				.forEach(moduleDto -> {

							// List<Module> matchingModules	= moduleRepository.findByDeviceIdAndType(device.getId(), moduleDto.getType());
							device.getModules()
									.stream()
									.filter(module -> module.getType().equals(moduleDto.getType()))
									.forEach(module -> {
												module.setState(moduleDto.getState());
												module.setValue(moduleDto.getValue());
											}
									);
						}
				);
	}
}
