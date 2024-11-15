package co.kr.common.config;

import co.kr.common.handler.CommonExceptionHandler;
import co.kr.common.mapper.CustomObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Import(CommonExceptionHandler.class)
public class CommonConfigurer {
  public static ObjectMapper customObjectMapper() {
    return CustomObjectMapper.getObjectMapper();
  }

  public static PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
