package co.kr.common.converter;

import co.kr.common.code.UserType;
import co.kr.common.converter.shared.AbstractBaseEnumConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
public class UserTypeConverter extends AbstractBaseEnumConverter<UserType, String> {
    @Override
    protected String getEnumName() {
        return UserType.class.getSimpleName();
    }

    @Override
    protected UserType[] getValueList() {
        return UserType.values();
    }
}
