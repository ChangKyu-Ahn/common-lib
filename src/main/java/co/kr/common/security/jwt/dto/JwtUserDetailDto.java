package co.kr.common.security.jwt.dto;

import co.kr.common.code.UserType;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUserDetailDto {
	String userId;
	String type;

	public static JwtUserDetailDto newInstance(String userId, UserType type) {
		if (StringUtils.isAnyBlank(userId) || Objects.isNull(type)) {
			return null;
		}

		return new JwtUserDetailDto(userId, type.getCode());
	}

	public static JwtUserDetailDto newInstance(String userId, String type) {
		if (StringUtils.isAnyBlank(userId, type)) {
			return null;
		}

		return new JwtUserDetailDto(userId, type);
	}
}
