package org.antonakospanos.iot.atlas.dao.converter;

import org.antonakospanos.iot.atlas.dao.model.Account;
import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.dao.repository.DeviceRepository;
import org.antonakospanos.iot.atlas.service.HashService;
import org.antonakospanos.iot.atlas.web.dto.accounts.AccountDto;
import org.antonakospanos.iot.atlas.web.dto.patch.PatchDto;
import org.antonakospanos.iot.atlas.web.dto.patch.PatchOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class AccountConverter {

	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	HashService hashService;

	public void setPassword(AccountDto accountDto, Account account) {
		String hashedPassword = hashService.bCryptPassword(accountDto.getPassword());
		account.setPassword(hashedPassword);
	}

	public void addDevices(AccountDto accountDto, Account account) {
		Set<Device> devices = new HashSet<>();

		List<String> deviceIds = accountDto.getDevices();
		if (deviceIds != null) {
			deviceIds
				.stream()
				.forEach(deviceExternalId -> {
					Device device = deviceRepository.findByExternalId(deviceExternalId);
					if (device != null) {
						devices.add(device);
					} else {
						throw new IllegalArgumentException("Device '" + deviceExternalId + "' does not exist!");
					}
				});
		}

		account.setDevices(devices);
	}

	public void patchAccount(PatchDto patchDto, Account account) {
		String field = patchDto.getField();
		PatchOperation operation = patchDto.getOperation();
		String value = patchDto.getValue();

		if ("username".equals(field)) {
			account.setUsername(value);
		} else if ("password".equals(field)) {
			String hashedPassword = hashService.bCryptPassword(value);
			account.setPassword(hashedPassword);
			account.setExternalId(UUID.randomUUID()); // Update externalId used in Authentication Bearer too!
		} else if ("name".equals(field)) {
			account.setName(value);
		} else if ("email".equals(field)) {
			account.setEmail(value);
		} else if ("cellphone".equals(field)) {
			account.setCellphone(value);
		} else if ("devices".equals(field)) {

			if (PatchOperation.ADD.equals(operation)) {
				Device device = deviceRepository.findByExternalId(value);
				account.addDevice(device);
			} else if (PatchOperation.REMOVE.equals(operation) && StringUtils.isNotBlank(value)) {
				Device device = deviceRepository.findByExternalId(value);
				account.removeDevice(device);
			} else if (PatchOperation.REMOVE.equals(operation) && StringUtils.isBlank(value)) {
				account.removeAll();
			}
		}
	}
}