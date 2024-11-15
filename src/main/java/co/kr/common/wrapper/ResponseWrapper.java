package co.kr.common.wrapper;

import co.kr.common.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseWrapper implements ResponseBodyAdvice<Object> {

	private final ObjectMapper objectMapper;

	@Override
	public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(
		Object responseBody,
		@NonNull MethodParameter methodParameter,
		@NonNull MediaType selectedContentType,
		@NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
		@NonNull ServerHttpRequest request,
		@NonNull ServerHttpResponse response
	) {
		HttpStatus responseStatus = getResponseStatus(methodParameter);

		if (responseStatus.isError()) {
			return responseBody;
		}

		return getEnvelopedResponseEntity(responseBody, responseStatus);
	}

	private HttpStatus getResponseStatus(MethodParameter methodParameter) {
		return Optional.ofNullable(methodParameter.getMethodAnnotation(ResponseStatus.class))
			.map(ResponseStatus::value)
			.orElse(HttpStatus.OK);
	}

	private Object getEnvelopedResponseEntity(Object responseBody, HttpStatus responseStatus) {
		if (isResponseEntity(responseBody)) {
			return responseBody;
		}

		return getResponseDto(responseBody, responseStatus);
	}

	private boolean isResponseEntity(Object responseBody) {
		return responseBody instanceof ResponseEntity;
	}

	private Object getResponseDto(Object responseBody, HttpStatus responseStatus) {
		if (Objects.isNull(responseBody)) {
			return null;
		}

		ResponseDto<Object> responseDto = ResponseDto.builder()
			.code(responseStatus.value())
			.response(responseBody)
			.build();

		return responseBody instanceof String ? getConvertedValue(responseDto) : responseDto;
	}

	private Object getConvertedValue(Object responseBody) {
		try {
			return objectMapper.writeValueAsString(responseBody);
		} catch (Exception e) {
			log.error("#### String Data Response ERROR : {}", e.getMessage(), e);
			return responseBody;
		}
	}
}