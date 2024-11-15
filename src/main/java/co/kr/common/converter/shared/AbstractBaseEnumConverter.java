package co.kr.common.converter.shared;


import co.kr.common.code.BaseEnumCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;

@Converter
public abstract class AbstractBaseEnumConverter<X extends Enum<X> & BaseEnumCode<Y>, Y> implements AttributeConverter<X, Y> {

	protected abstract String getEnumName();

	protected abstract X[] getValueList();

	@Override
	public Y convertToDatabaseColumn(X attribute) {
		return attribute == null ? null : attribute.getCode();
	}

	@Override
	public X convertToEntityAttribute(Y dbData) {
		return dbData == null ? null : Arrays.stream(getValueList())
			.filter(e -> e.getCode().equals(dbData))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(String.format("Enum %s에 Code %s가 존재하지 않습니다.", getEnumName(), dbData)));
	}
}
