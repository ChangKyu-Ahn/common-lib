package co.kr.common.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class SpecificationException extends BizException {
    private final List<String> errorMessages;

    public SpecificationException(String message) {
        super(message);
        this.errorMessages = new ArrayList<>();
    }
}
