package co.kr.common.specification;

import co.kr.common.specification.shared.Specification;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class DataOwnerSpec extends Specification<String> {

	private final String checkTargetUserId;

	@Override
	protected boolean check(String target) {
		return StringUtils.isNoneBlank(target, checkTargetUserId) && StringUtils.equals(target, checkTargetUserId);
	}

	@Override
	public String getErrorMessage() {
		return "데이터 접근 권한이 없는 요청 입니다";
	}
}
