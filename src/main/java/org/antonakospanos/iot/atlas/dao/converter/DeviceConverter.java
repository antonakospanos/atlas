package org.antonakospanos.iot.atlas.dao.converter;

import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.web.dto.devices.DeviceDto;
import org.antonakospanos.iot.atlas.web.dto.devices.ModuleDto;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DeviceConverter {

	public Device createDevice(DeviceDto deviceDto) {

		List<Module> modules = new ArrayList<>();
		Device device = new Device(null, deviceDto.getVersion(), deviceDto.getId(), ZonedDateTime.now(), modules);

		deviceDto.getModules()
				.stream()
				.forEach(dto ->	{
					Module module = new Module(device, dto.getId(), dto.getName(), dto.getType(), dto.getState(), dto.getValue(), null);
					modules.add(module);
				});

			return device;
	}

	public void updateDevice(DeviceDto deviceDto, Device device) {

		device.setExternalId(deviceDto.getId());
		device.setVersion(deviceDto.getVersion());
		device.setLastContact(ZonedDateTime.now());

		Map<String, ModuleDto> newModules = deviceDto.getModules().stream().collect(Collectors.toMap(ModuleDto::getId, Function.identity()));
		Map<String, Module> oldModules = device.getModules().stream().collect(Collectors.toMap(Module::getExternalId, Function.identity()));

		deviceDto.getModules()
				.stream()
				.forEach(moduleDto -> {

							if (!oldModules.containsKey(moduleDto.getId())) {
								// Add new module
								Module newModule = moduleDto.toEntity();
								device.addModule(newModule);
							} else if (oldModules.containsKey(moduleDto.getId())) {
								// Update old module
								Module oldModule = oldModules.get(moduleDto.getId());
								moduleDto.toEntity(oldModule);
							}
						}
				);

		// Remove missing modules
		for (Iterator<Module> i = device.getModules().listIterator(); i.hasNext(); ) {
			Module module = i.next();
			if (!newModules.containsKey(module.getExternalId())) {
				i.remove();
			}
		}
	}
}
