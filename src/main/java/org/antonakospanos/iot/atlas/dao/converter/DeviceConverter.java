package org.antonakospanos.iot.atlas.dao.converter;

import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.web.dto.DeviceDto;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DeviceConverter {

	public Device createDevice(DeviceDto deviceDto) {

		List<Module> modules = new ArrayList<>();
		Device device = new Device(null, deviceDto.getVersion(), deviceDto.getId(), ZonedDateTime.now(), modules);

		deviceDto.getModules()
				.stream()
				.forEach(dto ->	{
					Module module = new Module(device, dto.getType(), dto.getState(), dto.getValue(), null);
					modules.add(module);
				});

			return device;
	}

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
