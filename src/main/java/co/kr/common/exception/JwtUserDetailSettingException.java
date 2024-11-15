package co.kr.common.exception;

import lombok.Getter;

@Getter
public class JwtUserDetailSettingException extends RuntimeException {

    public JwtUserDetailSettingException() {
        super("JWT Token을 통한 사용자 정보 세팅에 실패하였습니다");
    }
}
