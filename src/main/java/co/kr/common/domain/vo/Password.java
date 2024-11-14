package co.kr.common.domain.vo;

import static co.kr.common.config.CommonConfigurer.customPasswordEncoder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.ObjectUtils;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Password {
	String value;

	public static Password newInstance(String value) {
		if (ObjectUtils.isEmpty(value)) {
			return null;
		}

		return new Password(customPasswordEncoder().encode(value));
 	}
}
