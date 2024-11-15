package co.kr.common.util;

import java.util.Optional;
import java.util.function.Function;

public class MapperUtil {

	public static <T, R> R getValue(T value, Function<T, R> mapper) {
		return Optional.ofNullable(value)
			.map(mapper)
			.orElse(null);
	}
}