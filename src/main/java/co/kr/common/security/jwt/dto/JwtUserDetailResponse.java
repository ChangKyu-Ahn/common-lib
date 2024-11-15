package co.kr.common.security.jwt.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class JwtUserDetailResponse implements Serializable {
  private static final long serialVersionUID = 1019953370121850344L;
  private String userId;
  private String accessToken;
  private String refreshToken;
}
