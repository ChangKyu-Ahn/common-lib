package co.kr.common.domain.vo;


import static co.kr.common.config.CommonConfigurer.passwordEncoder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Password {
	String value;

	public static Password newInstance(String value) {
		if (ObjectUtils.isEmpty(value)) {
			return null;
		}

		return new Password(value);
 	}

	 public static Password newInstanceWithEncrypted(@NonNull String value) {
		return newInstance(passwordEncoder().encode(value));
	 }

	public static void main(String[] args) {
	        String rawPassword = "test1234";
	        String encodedPassword = "$2a$10$7USoea5CtD6iA/w5e9uhO.g5hK65EArbFXygJq9sry2vmLtiGOgLu";

	        // BCryptPasswordEncoder 생성
	        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	        // {bcrypt} 접두사를 제거 (Spring Security는 자동으로 처리하기도 함)
	        encodedPassword = encodedPassword.replace("{bcrypt}", "");

	        // 비밀번호 비교
	        boolean isMatch = passwordEncoder.matches(rawPassword, encodedPassword);

	        if (isMatch) {
	            System.out.println("비밀번호가 일치합니다!");
	        } else {
	            System.out.println("비밀번호가 일치하지 않습니다.");
	        }
	    }
}
