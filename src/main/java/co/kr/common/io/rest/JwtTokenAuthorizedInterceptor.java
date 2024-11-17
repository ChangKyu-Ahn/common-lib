package co.kr.common.io.rest;

import co.kr.common.security.jwt.JwtAuthenticationToken;
import java.io.IOException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtTokenAuthorizedInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken());
		return execution.execute(request, body);
	}

	private String getAccessToken() {
		JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		if (ObjectUtils.allNotNull(authentication, authentication.getPrincipal())) {
			return authentication.getToken();
		}

		return null;
	}
}