package co.kr.common.util;

import co.kr.common.code.UserType;
import co.kr.common.exception.InvalidTokenException;
import co.kr.common.security.jwt.JwtAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class SecurityUtil {

	public static String getUserId() {
		Authentication authentication = getAuthentication();

		User user = (User) authentication.getPrincipal();
		return user.getUsername();
	}

	public static boolean isAdmin() {
		Authentication authentication = getAuthentication();

		return authentication.getAuthorities().stream()
			.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_" + UserType.SYS_ADMIN.getCode()));
	}

	private static Authentication getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (!(authentication instanceof JwtAuthenticationToken)) {
			throw new InvalidTokenException("알 수 없는 인증 정보");
		}

		return authentication;
	}
}
