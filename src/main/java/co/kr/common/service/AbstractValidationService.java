package co.kr.common.service;

import co.kr.common.specification.DataOwnerSpec;
import co.kr.common.specification.SpecificationExecutor;
import co.kr.common.util.SecurityUtil;

public class AbstractValidationService {
	public static void checkDataOwner(String targetUserId) {
		if (SecurityUtil.isAdmin()) {
			return;
		}

		SpecificationExecutor specificationExecutor = new SpecificationExecutor();
		specificationExecutor.addSpecification(new DataOwnerSpec(targetUserId), SecurityUtil.getUserId());
		specificationExecutor.validateAll();
	}
}
