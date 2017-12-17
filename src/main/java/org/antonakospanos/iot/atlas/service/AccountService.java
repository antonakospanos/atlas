package org.antonakospanos.iot.atlas.service;

import org.antonakospanos.iot.atlas.dao.model.Account;
import org.antonakospanos.iot.atlas.dao.repository.AccountRepository;
import org.antonakospanos.iot.atlas.web.dto.accounts.AccountDto;
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
public class AccountService {

	private final static Logger logger = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	AccountRepository accountRepository;

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
