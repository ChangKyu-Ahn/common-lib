package co.kr.common.security.jwt;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	@Getter
	private final String token;
	private final Object principal;

	public JwtAuthenticationToken(String token) {
		super(null);
		this.token = token;
		this.principal = null; // 아직 인증되지 않은 상태
		setAuthenticated(false);
	}

	public JwtAuthenticationToken(Object principal, String token, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.token = token;
		this.principal = principal;
		setAuthenticated(true); // 인증된 상태
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}
}