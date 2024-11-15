package co.kr.common.util;

import co.kr.common.exception.InvalidTokenException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class SecurityUtil {
    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
            throw new InvalidTokenException("알 수 없는 인증 정보");
        }

        User user = (User) authentication.getPrincipal();
        return user.getUsername();
    }
}
