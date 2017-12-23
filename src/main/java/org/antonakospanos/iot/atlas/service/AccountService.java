package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.dao.model.Account;
import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.dao.repository.AccountRepository;
import org.antonakospanos.iot.atlas.dao.repository.DeviceRepository;
import org.antonakospanos.iot.atlas.web.dto.accounts.AccountDto;
import org.antonakospanos.iot.atlas.web.dto.accounts.AccountRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService {

	private final static Logger logger = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	DeviceRepository deviceRepository;


	@Transactional
	public void create(AccountRequest request) {

		AccountDto accountDto = request.getAccountDto();
		Account account = accountRepository.findByUsername(accountDto.getUsername());

		if (account != null) {
			throw new IllegalArgumentException("Account with username '" + account.getUsername() + "' already exists!");
		} else {
			// Add new Account in DB
			account = accountDto.toEntity();

			Set<Device> devices = new HashSet<>();
			accountDto.getDevices()
					.stream()
					.forEach(deviceExternalId -> {
							Device device = deviceRepository.findByExternalId(deviceExternalId);
							if (device != null) {
								devices.add(device);
							} else {
								throw new IllegalArgumentException("Device '" + deviceExternalId + "' does not exist!");
							}
					});

			account.setDevices(devices);
			accountRepository.save(account);
		}
	}

	@Transactional
	public void delete(String username) {
		Account account = accountRepository.findByUsername(username);

		if (account == null) {
			throw new IllegalArgumentException("Account '" + account + "' does not exist!");
		} else {
			accountRepository.delete(account);
		}
	}

	@Transactional
	public List<AccountDto> list(String username) {
		List<AccountDto> accountDtos = new ArrayList<>();

		if (StringUtils.isNotBlank(username)) {
			Account account = accountRepository.findByUsername(username);

			AccountDto accountDto = new AccountDto().fromEntity(account);
			accountDtos.add(accountDto);
		} else {
			List<Account> accounts = accountRepository.findAll();

			accountDtos = accounts.stream()
					.map(account -> new AccountDto().fromEntity(account))
					.collect(Collectors.toList());

		}

		return accountDtos;
	}
}
