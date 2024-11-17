package co.kr.common.security.jwt;

import co.kr.common.exception.InvalidTokenException;
import co.kr.common.security.jwt.dto.JwtUserDetailDto;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JWTRequestFilter extends OncePerRequestFilter {
	private final AntPathMatcher pathMatcher = new AntPathMatcher();
	private final JwtUserDetailsService jwtUserDetailsService;
	private final String[] EXCLUDE_URL;

	public JWTRequestFilter(JwtUserDetailsService jwtUserDetailsService, String[] excludeURL) {
		this.jwtUserDetailsService = jwtUserDetailsService;
		EXCLUDE_URL = excludeURL;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws IOException {
		try {
			validateAndSetAuthentication(request);
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			log.error("Authorized Error : {}", e.getMessage(), e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		}

	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		if (ObjectUtils.isEmpty(EXCLUDE_URL)) {
			return false;
		}

		String requestUri = request.getRequestURI();
		return Arrays.stream(EXCLUDE_URL).anyMatch(pattern -> pathMatcher.match(pattern, requestUri));
	}

	private void setAuthentication(HttpServletRequest request, JwtUserDetailDto jwtUserDetailDto, String accessToken) {
		try {
			UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(jwtUserDetailDto);

			JwtAuthenticationToken usernamePasswordAuthenticationToken = new JwtAuthenticationToken(userDetails, accessToken, userDetails.getAuthorities());
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		} catch (Exception e) {
			log.error("Unexpected Exception : ", e);
			throw e;
		}
	}

	private String getAccessToken(HttpServletRequest request) {
		String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (StringUtils.isBlank(accessToken)) {
			throw new InvalidTokenException("no accessToken");
		}

		return accessToken.replace("Bearer ", StringUtils.EMPTY);
	}

	private void validateAndSetAuthentication(HttpServletRequest request) {
		String accessToken = getAccessToken(request);
		Claims claims = JwtTokenUtil.extractAllClaims(accessToken);

		JwtUserDetailDto jwtUserDetailDto = JwtTokenUtil.getJwtUserDetailDto(claims);

		if (ObjectUtils.isEmpty(jwtUserDetailDto)) {
			throw new InvalidTokenException("no necessary token Detail Info");
		}

		setAuthentication(request, jwtUserDetailDto, accessToken);
	}
}
