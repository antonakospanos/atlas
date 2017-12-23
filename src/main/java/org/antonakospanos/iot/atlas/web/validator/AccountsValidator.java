package org.antonakospanos.iot.atlas.web.validator;

import org.antonakospanos.iot.atlas.web.dto.accounts.AccountRequest;

public class AccountsValidator {

	public static void validateAccount(AccountRequest create) {

//		List<String> moduleList = create.getDevice().getModules()
//				.stream()
//				.map(module -> module.getId())
//				.collect(Collectors.toList());
//
//		Set<String> moduleSet = new HashSet<String>(moduleList);
//
//		if (moduleList.size() > moduleSet.size()) {
//			throw new IllegalArgumentException("Device's Module IDs shall differ: " + moduleList);
//		}
	}
}