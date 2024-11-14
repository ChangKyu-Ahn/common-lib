package co.kr.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class CommonConfigurer {
  @Bean
  public static ObjectMapper customObjectMapper() {
    return Jackson2ObjectMapperBuilder
      .json()
      .modules(new JavaTimeModule(), new BlackbirdModule())
      .build();
  }
}
