package co.kr.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

public class JwtTokenRestClientConfigurer {

	@Autowired
	private ObjectMapper objectMapper;

	public JwtTokenRestClientConfigurer() {
	}

	protected RestClient jwtTokenRestClient(int connectTimeout, int readTimeOut, String baseUrl, ClientHttpRequestInterceptor clientHttpRequestInterceptor) {
		return restClient(connectTimeout, readTimeOut, baseUrl, clientHttpRequestInterceptor);
	}

	protected RestClient restClient(int connectTimeout, int readTimeOut, String baseUrl, ClientHttpRequestInterceptor clientHttpRequestInterceptor) {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory() {};

		requestFactory.setConnectTimeout(Duration.ofSeconds(connectTimeout));
		requestFactory.setConnectionRequestTimeout(Duration.ofSeconds(readTimeOut));

		return RestClient.builder()
			.baseUrl(baseUrl)
			.requestFactory(requestFactory)
			.messageConverters(converters -> converters.addFirst(new MappingJackson2HttpMessageConverter(objectMapper)))
			.requestInterceptors(interceptors -> {
				if (clientHttpRequestInterceptor != null) {
					interceptors.add(clientHttpRequestInterceptor);
				}
			})
			.build();
	}
}
