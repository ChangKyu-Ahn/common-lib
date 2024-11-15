package co.kr.common.code;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType implements BaseEnumCode<String> {
	SYS_ADMIN("SAN", "관리자"),
	CUSTOMER("CTR", "고객"),
	;

	private final String code;
	private final String desc;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	public static UserType getEnumByCode(String code) {
		return Arrays.stream(values())
			.filter(e -> e.getCode().equals(code))
			.findFirst()
			.orElse(null);
	}
}
