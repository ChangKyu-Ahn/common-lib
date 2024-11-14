package co.kr.common.config;

import co.kr.common.handler.CommonExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Import(CommonExceptionHandler.class)
public class CommonConfigurer {
  @Bean
  public static ObjectMapper customObjectMapper() {
    return Jackson2ObjectMapperBuilder
      .json()
      .modules(new JavaTimeModule(), new BlackbirdModule())
      .build();
  }

  public static PasswordEncoder customPasswordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
