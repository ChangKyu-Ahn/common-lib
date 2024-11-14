package co.kr.common.handler;

import co.kr.common.dto.ErrorFieldDto;
import co.kr.common.dto.ResponseDto;
import co.kr.common.exception.BizException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ResponseDto<Object> exception(Exception e) {
    log.error(e.getMessage(), e);

    return new ResponseDto<>(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        null);
  }

  @ExceptionHandler
  @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
  @ResponseBody
  public ResponseDto<Object> bizException(BizException e) {
    int code = e.getCode() == 0 ? HttpStatus.UNPROCESSABLE_ENTITY.value() : e.getCode();
    return new ResponseDto<>(code, null, "Invalid Request", e.getMessage());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    return handleExceptionInternal(ex, validException(ex), headers, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    return handleExceptionInternal(ex, validException(ex), headers, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler
  @ResponseBody
  public ResponseEntity<?> dataIntegrityViolationException(DataIntegrityViolationException e) {
    Throwable throwable = e.getRootCause();

    boolean isDuplicateError = throwable.getClass().getName().equals("org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException") && ((SQLException) throwable).getSQLState().equals("23505");

    if (isDuplicateError) {
      ResponseDto<Object> responseDto = bizException(new BizException("중복된 데이터가 있습니다"));
      return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    } else {
      exception(e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  private ResponseDto<List<ErrorFieldDto>> validException(Exception e) {
    List<ErrorFieldDto> errorFieldList = new ArrayList<>();
    ErrorFieldDto errorField = null;
    if (e instanceof MethodArgumentNotValidException) {
      List<FieldError> errors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
      List<ErrorFieldDto> convertedErrorFieldList = //
        errors.stream()
          .map(error -> getErrorField(error.getField(), error.getDefaultMessage()))
          .toList();

      errorFieldList.addAll(convertedErrorFieldList);
    } else if (e instanceof HttpMessageNotReadableException) {
      errorField = getErrorField("Request Body", "Request Body is empty");
    }

    if (ObjectUtils.isNotEmpty(errorField)) {
      errorFieldList.add(errorField);
    }

    return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), null, "Invalid Request", errorFieldList);
  }

  private ErrorFieldDto getErrorField(String field, String message) {
    return ErrorFieldDto.builder()
      .field(field)
      .message(message)
      .build();
  }
}