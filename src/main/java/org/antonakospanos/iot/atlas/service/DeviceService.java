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
import java.util.UUID;
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
	public List<DeviceDto> listByAccountId(String deviceId, UUID accountExternalId) {
		List<DeviceDto> deviceDtos = new ArrayList<>();

		// Validate listed resources
		validateDevice(deviceId);
		accountService.validateAccount(accountExternalId);

		if (StringUtils.isNotBlank(deviceId) && accountExternalId != null) {
			// Fetch user's device with the declared id
			Account account = accountRepository.findByExternalId(accountExternalId);
			deviceDtos = account.getDevices().stream()
					.filter(device -> device.getExternalId().equals(deviceId))
					.map(device -> new DeviceDto().fromEntity(device))
					.collect(Collectors.toList());

		} else if (StringUtils.isNotBlank(deviceId)) {
			// Fetch device with the declared id
			Device device = deviceRepository.findByExternalId(deviceId);

			DeviceDto deviceDto = new DeviceDto().fromEntity(device);
			deviceDtos.add(deviceDto);

		} else if (accountExternalId != null) {
			// Fetch all user's devices
			Account account = accountRepository.findByExternalId(accountExternalId);
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

	public List<DeviceDto> listByUsername(String deviceId, String username) {
		accountService.validateAccount(username);

		List<DeviceDto> deviceDtos;
		if (StringUtils.isNotBlank(username)) {
			Account account = accountRepository.findByUsername(username);
			deviceDtos = listByAccountId(deviceId, account.getExternalId());
		} else {
			deviceDtos = listByAccountId(deviceId, null);
		}

		return deviceDtos;
	}

	@Transactional
	public List<DeviceDto> list(String deviceId) {
		return listByAccountId(deviceId, null);
	}

	@Transactional
	public List<DeviceDto> listAll() {
		return listByAccountId(null, null);
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
