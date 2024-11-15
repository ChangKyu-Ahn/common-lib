package co.kr.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BizException  {
    public InvalidTokenException(String message) {
        super(HttpStatus.UNAUTHORIZED.value(), message);
    }
}
