package co.kr.common.exception;


public class NotExistDataException extends BizException {

	public NotExistDataException() {
		this("존재하지 않는 데이터 입니다");
	}

	public NotExistDataException(String message) {
		super(422, message);
	}
}
