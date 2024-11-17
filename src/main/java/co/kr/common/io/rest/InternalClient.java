package co.kr.common.io.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;

@Slf4j
public class InternalClient extends CommonApi {

  public InternalClient(String apiServerName, RestClient restClient) {
    super(apiServerName, restClient);
  }
}
