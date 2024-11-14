package co.kr.common.exception;

import lombok.Getter;

public class BizException extends RuntimeException {
    @Getter
    private int code;

    private Object[] data;

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(Throwable e) {
        super(e);
    }
}