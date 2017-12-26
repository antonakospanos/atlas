package org.antonakospanos.iot.atlas.web.validator;

import org.antonakospanos.iot.atlas.web.dto.actions.ActionRequest;

public class ActionsValidator {

	public static void validateAction(ActionRequest create) {

		if (create.getAlert() &&
							(create.getAction().getCondition() == null
						|| create.getAction().getCondition().getOrLegs() == null
						|| create.getAction().getCondition().getOrLegs().isEmpty())) {
			throw new IllegalArgumentException("Alerts are explicitly supported in case of conditional actions");
		}
	}
}