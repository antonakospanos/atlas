package org.antonakospanos.iot.atlas.web.validator;

import org.antonakospanos.iot.atlas.web.dto.actions.ActionRequest;

public class ActionsValidator {

	public static void validateAction(ActionRequest create) {

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