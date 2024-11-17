package co.kr.common.api;

import co.kr.common.io.rest.InternalClient;
import co.kr.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Slf4j
public class BoardApi {

	private final InternalClient internalClient;

	public BoardApi(InternalClient internalClient) {
		this.internalClient = internalClient;
	}

	@Async
	public void deleteAllByUserId(String userId) {
		try {
			String path = SecurityUtil.isAdmin() ? String.format("/boards/user/%s", userId) : "/v1/boards/user" ;
			internalClient.post(path, null, null);
		} catch (Exception e) {
			log.error("#### 사용자 게시글 삭제 오류 : {}", e.getMessage(), e);
		}
	}
}
