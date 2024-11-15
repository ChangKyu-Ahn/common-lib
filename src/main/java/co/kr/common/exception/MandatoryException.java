package co.kr.common.exception;


public class MandatoryException extends BizException {

	public MandatoryException() {
		this("필수 입력 필드가 존재합니다");
	}

	public MandatoryException(String message) {
		super(422, message);
	}
}
