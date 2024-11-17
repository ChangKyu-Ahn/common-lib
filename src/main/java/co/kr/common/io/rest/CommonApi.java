package co.kr.common.io.rest;

import co.kr.common.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestBodySpec;

@Slf4j
public class CommonApi {
	@Autowired
	protected ObjectMapper objectMapper;

	@Getter
	private final RestClient restClient;

	@Getter
	private final String apiServerName;

	public CommonApi(String apiServerName, RestClient restClient) {
		this.restClient = restClient;
		this.apiServerName = apiServerName;
	}

	/**
	 * GET 요청
	 */
	public <T> ResponseDto<T> get(String path, Class<T> responseType, Object... uriVariables) {
		return execute(HttpMethod.GET, path, null, responseType, uriVariables);
	}

	/**
	 * POST 요청
	 */
	public <T, P> ResponseDto<T> post(String path, P body, Class<T> responseType, Object... uriVariables) {
		return execute(HttpMethod.POST, path, body, responseType, uriVariables);
	}

	/**
	 * 공통 요청 처리 메서드
	 */
	private <T, P> ResponseDto<T> execute(HttpMethod method, String path, P body, Class<T> responseType, Object... uriVariables) {
		try {
			RequestBodySpec requestSpec = restClient
					.method(method)
					.uri(path, uriVariables)
					.accept(MediaType.APPLICATION_JSON);

			if (body != null) {
				requestSpec.body(body);
			}

			return requestSpec.exchange((request, response) -> {
				if (response.getStatusCode().is2xxSuccessful()) {
					T responseBody = response.bodyTo(responseType);
					return ResponseDto.<T>builder()
							.code(response.getStatusCode().value())
							.response(responseBody)
							.build();
				}

				return handleErrorResponse(response);
			});
		} catch (Exception e) {
			log.error("Error during API call to [{}]: {}", path, e.getMessage(), e);
			return ResponseDto.<T>builder()
					.code(500)
					.error(e.getMessage())
					.build();
		}
	}

	/**
	 * 에러 응답 처리
	 */
	private <T> ResponseDto<T> handleErrorResponse(
			RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse response) throws IOException {
		try {
			byte[] responseBody = response.getBody().readAllBytes();
			String errorMessage = new String(responseBody);

			log.error("API error response: {}", errorMessage);

			return ResponseDto.<T>builder()
				.code(response.getStatusCode().value())
				.error(errorMessage)
				.build();
		} catch (Exception e) {
			log.error("Error reading error response: {}", e.getMessage(), e);
			return ResponseDto.<T>builder()
				.code(response.getStatusCode().value())
				.error(response.getStatusText())
				.build();
		}
	}
}