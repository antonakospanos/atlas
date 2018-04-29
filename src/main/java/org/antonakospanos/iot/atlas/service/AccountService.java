package org.antonakospanos.iot.atlas.service;

import javassist.NotFoundException;
import org.antonakospanos.iot.atlas.dao.converter.AccountConverter;
import org.antonakospanos.iot.atlas.dao.model.Account;
import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.dao.repository.AccountRepository;
import org.antonakospanos.iot.atlas.dao.repository.DeviceRepository;
import org.antonakospanos.iot.atlas.web.dto.accounts.AccountDto;
import org.antonakospanos.iot.atlas.web.dto.accounts.AccountRequest;
import org.antonakospanos.iot.atlas.web.dto.patch.PatchDto;
import org.antonakospanos.iot.atlas.web.dto.response.CreateResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

	private final static Logger logger = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	AccountConverter accountConverter;

	@Autowired
	HashService hashService;


	@Transactional
	public CreateResponseData create(AccountRequest request) {

		AccountDto accountDto = new AccountDto(request.getAccount());
		Account account = accountRepository.findByUsername(accountDto.getUsername());

		if (account != null) {
			throw new IllegalArgumentException("Account with username '" + account.getUsername() + "' already exists!");
		} else {
			// Add new Account in DB
			account = accountDto.toEntity();
			accountConverter.updateAccount(accountDto, account);

			accountRepository.save(account);
		}

		return new CreateResponseData(account.getExternalId().toString());
	}

	@Transactional
	public void replace(String username, AccountRequest request) {

		AccountDto accountDto = new AccountDto(request.getAccount());
		validateAccount(username);

		// Update Account in DB
		Account account = accountRepository.findByUsername(username);
		replace(accountDto, account);
	}


	@Transactional
	public void replace(UUID accountExternalId, AccountRequest request) {

		AccountDto accountDto = new AccountDto(request.getAccount());
		validateAccount(accountExternalId);

		// Update Account in DB
		Account account = accountRepository.findByExternalId(accountExternalId);
		replace(accountDto, account);
	}

	@Transactional
	public void replace(AccountDto accountDto, Account account) {
		validateNewUsername(account.getUsername(), accountDto.getUsername());

		accountDto.toEntity(account);
		accountConverter.updateAccount(accountDto, account);
		accountRepository.save(account);
	}

	@Transactional
	public void update(String username, List<PatchDto> patches) {
		validateAccount(username);
		Account account = accountRepository.findByUsername(username);

		update(account, patches);
	}

	@Transactional
	public void update(UUID accountExternalId, List<PatchDto> patches) {
		validateAccount(accountExternalId);
		Account account = accountRepository.findByExternalId(accountExternalId);

		update(account, patches);
	}

	@Transactional
	public void update(Account account, List<PatchDto> patches) {

		patches.stream().forEach(patchDto -> {
			// Validate Account patches
			if (patchDto.getField().equals("username")) {
				validateNewUsername(account.getUsername(), patchDto.getValue());
			} else if (patchDto.getField().equals("devices")) {
				validateNewDevice(patchDto.getValue());
			}
			// Update Account in DB
			accountConverter.updateAccount(patchDto, account);
		});

		accountRepository.save(account);
	}

	@Transactional
	public void delete(String username) {
		Account account = accountRepository.findByUsername(username);
		delete(account);
	}

	@Transactional
	public void delete(UUID accountExternalId) {
		Account account = accountRepository.findByExternalId(accountExternalId);
		delete(account);
	}

	@Transactional
	public void delete(Account account) {
		if (account == null) {
			throw new IllegalArgumentException("Account '" + account + "' does not exist!");
		} else {
			accountRepository.delete(account);
		}
	}

	@Transactional
	public AccountDto find(String username, String password) throws NotFoundException {
		List<AccountDto> accounts = list(username);

		if (accounts == null || accounts.isEmpty()) {
			throw new NotFoundException("No user found with username '" + username + "'");
		} else if (accounts.size() > 1) {
			throw new RuntimeException("Multiple accounts found with username '" + username + "'");
		} else {
			AccountDto accountDto = accounts.get(0);
			if (!hashService.matches(password, accountDto.getPassword())) {
				throw new AuthorizationServiceException("Invalid password '" + password + "'");
			} else {
				return accountDto;
			}
		}
	}

	@Transactional
	public List<AccountDto> list(String username) {
		List<AccountDto> accountDtos;

		if (StringUtils.isNotBlank(username)) {
			Account account = accountRepository.findByUsername(username);
			accountDtos = list(account);
		} else {
			accountDtos = listAll();
		}

		return accountDtos;
	}

	@Transactional
	public List<AccountDto> list(UUID accountExternalId) {
		List<AccountDto> accountDtos;

		if (accountExternalId != null) {
			Account account = accountRepository.findByExternalId(accountExternalId);
			accountDtos = list(account);
		} else {
			accountDtos = listAll();
		}

		return accountDtos;
	}

	@Transactional
	public List<AccountDto> list(Account account) {
		List<AccountDto> accountDtos = new ArrayList<>();

		if (account != null) {
			AccountDto accountDto = new AccountDto().fromEntity(account);
			accountDtos.add(accountDto);
		}

		return accountDtos;
	}

	@Transactional
	public List<AccountDto> listAll() {
		List<Account> accounts = accountRepository.findAll();

		List<AccountDto> accountDtos = accounts.stream()
				.map(account -> new AccountDto().fromEntity(account))
				.collect(Collectors.toList());

		return accountDtos;
	}

	@Transactional
	public void validateAccount(String username) {
		if (StringUtils.isNotBlank(username)) {
			Account account = accountRepository.findByUsername(username);
			if (account == null) {
				throw new IllegalArgumentException("Account with username '" + username + "' does not exist!");
			}
		}
	}

	@Transactional
	public void validateAccount(UUID accountId) {
		if (accountId != null) {
			Account account = accountRepository.findByExternalId(accountId);
			if (account == null) {
				throw new IllegalArgumentException("Account with id '" + accountId + "' does not exist!");
			}
		}
	}

	@Transactional
	public void validateNewUsername(String oldUsername, String newUsername) {
		if (StringUtils.isNotBlank(newUsername)) {
			// Check that resource does not conflict
			Account account = accountRepository.findByUsername(newUsername);
			if (!oldUsername.equals(newUsername) && account != null) {
				// Illegal username replacement
				throw new IllegalArgumentException("Account with username '" + newUsername + "' already exists!");
			}
		}
	}

	@Transactional
	public void validateNewDevice(String deviceExternalId) {
		if (StringUtils.isNotBlank(deviceExternalId)) {
			// Check that device resources exist
			Device device = deviceRepository.findByExternalId(deviceExternalId);
			if (device == null) {
				throw new IllegalArgumentException("Device with id '" + deviceExternalId + "' does not exist!");
			}
		}
	}
}
