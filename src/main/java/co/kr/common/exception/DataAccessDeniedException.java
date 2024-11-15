package co.kr.common.exception;

import org.springframework.http.HttpStatus;

public class DataAccessDeniedException extends BizException  {
    public DataAccessDeniedException() {
        this("데이터 접근 권한이 없는 요청 입니다.");
    }

    public DataAccessDeniedException(String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY.value(), message);
    }
}
