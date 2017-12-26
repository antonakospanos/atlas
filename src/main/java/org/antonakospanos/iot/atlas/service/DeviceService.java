package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.dao.converter.DeviceConverter;
import org.antonakospanos.iot.atlas.dao.model.Account;
import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.dao.repository.AccountRepository;
import org.antonakospanos.iot.atlas.dao.repository.DeviceRepository;
import org.antonakospanos.iot.atlas.web.dto.devices.DeviceDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceService {

	private final static Logger logger = LoggerFactory.getLogger(DeviceService.class);

	@Autowired
	AccountService accountService;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	DeviceConverter deviceConverter;

	@Transactional
	public Device put(DeviceDto deviceDto) {

		Device device = deviceRepository.findByExternalId(deviceDto.getId());

		if (device == null) {
			// Add new Device in DB
			device = deviceDto.toEntity(); // deviceConverter.createDevice(deviceDto);
			deviceRepository.save(device);

			logger.info("New Device added: " + deviceDto);

		} else {
			// Update Device information in DB
			deviceConverter.updateDevice(deviceDto, device); // deviceDto.toEntity(devices);
			deviceRepository.save(device);

			logger.debug("Device is updated: " + deviceDto);
		}

		return device;
	}

	@Transactional
	public List<DeviceDto> list(String deviceId, String username) {
		List<DeviceDto> deviceDtos = new ArrayList<>();

		// Validate listed resources
		validateDevice(deviceId);
		accountService.validateAccount(username);

		if (StringUtils.isNotBlank(deviceId) && StringUtils.isNotBlank(username)) {
			// Fetch user's device with the declared id
			Account account = accountRepository.findByUsername(username);
			deviceDtos = account.getDevices().stream()
					.filter(device -> device.getExternalId().equals(deviceId))
					.map(device -> new DeviceDto().fromEntity(device))
					.collect(Collectors.toList());

		} else if (StringUtils.isNotBlank(deviceId)) {
			// Fetch device with the declared id
			Device device = deviceRepository.findByExternalId(deviceId);

			DeviceDto deviceDto = new DeviceDto().fromEntity(device);
			deviceDtos.add(deviceDto);

		} else if (StringUtils.isNotBlank(username)) {
			// Fetch all user's devices
			Account account = accountRepository.findByUsername(username);
			deviceDtos = account.getDevices().stream()
					.map(device -> new DeviceDto().fromEntity(device))
					.collect(Collectors.toList());

		} else {
			// Fetch all devices
			List<Device> devices = deviceRepository.findAll();
			deviceDtos = devices.stream()
					.map(device -> new DeviceDto().fromEntity(device))
					.collect(Collectors.toList());
		}

		return deviceDtos;
	}

	@Transactional
	public void validateDevice(String deviceId) {
		if (StringUtils.isNotBlank(deviceId)) {
			Device device = deviceRepository.findByExternalId(deviceId);
			if (device == null) {
				throw new IllegalArgumentException("Device '" + deviceId + "' does not exist!");
			}
		}
	}
}
