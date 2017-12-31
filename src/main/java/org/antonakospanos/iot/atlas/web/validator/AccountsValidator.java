package org.antonakospanos.iot.atlas.web.validator;

import org.antonakospanos.iot.atlas.web.dto.accounts.AccountDto;
import org.antonakospanos.iot.atlas.web.dto.accounts.AccountRequest;
import org.antonakospanos.iot.atlas.web.dto.patch.PatchOperation;
import org.antonakospanos.iot.atlas.web.dto.patch.PatchRequest;
import org.apache.commons.lang3.StringUtils;

public class AccountsValidator {

	public static void validateAccount(AccountRequest create) {

		// Nothing yet
	}

	public static void validateAccount(PatchRequest request) {

		request.getPatches().stream().forEach(patchDto -> {

			String field = patchDto.getField();
			PatchOperation operation = patchDto.getOperation();
			String value = patchDto.getValue();

			// Field validation
			if (StringUtils.isBlank(field)) {
				throw new IllegalArgumentException("Field is required! Account resource: " + AccountDto.fields);
			} else if (!AccountDto.fields.contains(field)) {
				throw new IllegalArgumentException("Field '" + field + "' is not included in Account resource: " + AccountDto.fields);
			}

			// Operation ADD validation: Required fields can only be replaced
			if (patchDto.getOperation().equals(PatchOperation.ADD) &&
					(patchDto.getField().equals("username") || patchDto.getField().equals("password") || patchDto.getField().equals("email"))) {

				throw new IllegalArgumentException("Multiple "+patchDto.getField()+"s are not permitted");
			}
			// Operation REMOVE validation: Required fields can only be replaced
			if (patchDto.getOperation().equals(PatchOperation.REMOVE) && patchDto.getField().equals("username") ||
					(patchDto.getField().equals("username") || patchDto.getField().equals("password") || patchDto.getField().equals("email"))) {

				throw new IllegalArgumentException("Field "+patchDto.getField()+" is required and cannot be removed");
			}
			// Operation REPLACE validation
			if (patchDto.getOperation().equals(PatchOperation.REPLACE) && patchDto.getField().equals("devices")) {
				throw new IllegalArgumentException("Devices can be explicitly added or removed");
			}

			// Value validation
			if (PatchOperation.ADD.equals(operation) && StringUtils.isEmpty(value)) {
				throw new IllegalArgumentException("Value '" + value + "' is required in case of ADD operation");
			}
			if (PatchOperation.REPLACE.equals(operation) && StringUtils.isEmpty(value)) {
				throw new IllegalArgumentException("Value '" + value + "' is required in case of REPLACE operation");
			}
			if (PatchOperation.REMOVE.equals(operation) && StringUtils.isNotBlank(value)  && !field.equals("devices")) {
				// Value is used only in case of devices to define the deviceId that should be removed, else the whole array will be removed
				throw new IllegalArgumentException("Value '" + value + "' should be avoided in case of REMOVE operation (apart from field='devices' where value='deviceId' to be removed)");
			}
		});
	}
}