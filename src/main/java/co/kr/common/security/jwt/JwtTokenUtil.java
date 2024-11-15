package co.kr.common.security.jwt;

import co.kr.common.exception.BizException;
import co.kr.common.exception.MandatoryException;
import co.kr.common.security.jwt.dto.JwtUserDetailDto;
import co.kr.common.security.jwt.dto.JwtUserDetailResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenUtil {

	private final static int ACCESS_TOKEN_VALIDATION_SECOND = 60 * 60;
	private final static int REFRESH_TOKEN_VALIDATION_SECOND = 12 * 60 * 60;

	// 키 파일 경로
	private static final String PRIVATE_KEY_PATH = "private_key.pem";
	private static final String PUBLIC_KEY_PATH = "public_key.pem";

	// Private Key 로드
	private static PrivateKey getPrivateKey() throws Exception {
		String key = readKeyFromFile(PRIVATE_KEY_PATH).replaceAll("-----BEGIN PRIVATE KEY-----|-----END PRIVATE KEY-----|\\s", StringUtils.EMPTY);

		byte[] keyBytes = Base64.getDecoder().decode(key);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(spec);
	}

	// Public Key 로드
	private static PublicKey getPublicKey() throws Exception {
		String key = readKeyFromFile(PUBLIC_KEY_PATH).replaceAll("-----BEGIN PUBLIC KEY-----|-----END PUBLIC KEY-----|\\s", StringUtils.EMPTY);

		byte[] keyBytes = Base64.getDecoder().decode(key);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(spec);
	}

	// 키 파일 읽기
	private static String readKeyFromFile(String filePath) throws Exception {
		try (InputStream is = JwtTokenUtil.class.getClassLoader().getResourceAsStream(filePath)) {
			if (is == null) {
				throw new IllegalArgumentException("Key file not found: " + filePath);
			}
			return new String(is.readAllBytes());
		}
	}

	// JWT 토큰 관련한 모든 정보 Get
	public static Claims extractAllClaims(String token) {
		try {
			Jws<Claims> jwsClaims = Jwts.parser()
					.verifyWith(getPublicKey())
					.build()
					.parseSignedClaims(token);

			Claims claims = jwsClaims.getPayload();

			if (isTokenExpired(claims)) {
				throw new ExpiredJwtException(jwsClaims.getHeader(), claims, "Token has expired");
			}

			return claims;
		} catch (ExpiredJwtException expiredJwtException) {
			log.error("Token is Expired : {}", expiredJwtException.getMessage());
			throw new BizException(expiredJwtException.getMessage());
		} catch (JwtException jwtException) {
			log.error("Invalid Token : {}", jwtException.getMessage());
			throw new BizException("Invalid Token : " + jwtException.getMessage());
		} catch (Exception exception) {
			log.error("Unexpected exception : {}", exception.getMessage());
			throw new BizException("Unexpected exception : " + exception.getMessage());
		}
	}

	// Token 발급
	@SuppressWarnings("unchecked")
	public static JwtUserDetailResponse generateTokenAndSetTokenInfo(JwtUserDetailDto jwtUserDetailDto) throws Exception {
		return JwtUserDetailResponse.builder()
				.userId(jwtUserDetailDto.getUserId())
				.accessToken(doGenerateToken(jwtUserDetailDto, ACCESS_TOKEN_VALIDATION_SECOND))
				.refreshToken(doGenerateToken(jwtUserDetailDto, REFRESH_TOKEN_VALIDATION_SECOND))
				.build();
	}

	public static String doGenerateToken(JwtUserDetailDto jwtUserDetailDto, Integer expirationTime) throws Exception {
		return Jwts.builder()
				.claims(getUserDetailMap(jwtUserDetailDto))
				.subject(jwtUserDetailDto.getUserId())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
				.signWith(getPrivateKey())
				.compact();
	}

	private static boolean isTokenExpired(Claims claims) {
		try {
			if (ObjectUtils.isEmpty(claims)) {
				return true;
			}

			return new Date().after(claims.getExpiration());
		} catch (Exception e) {
			log.error("isTokenExpired error : {}", e.getMessage(), e);
			return true;
		}
	}

	public static JwtUserDetailDto getJwtUserDetailDto(Claims claims) {
		try {
			if (ObjectUtils.isEmpty(claims)) {
				return null;
			}

			return JwtUserDetailDto.newInstance(
				claims.get("userId", String.class),
				claims.get("type", String.class)
			);
		} catch (Exception e) {
			log.error("getUserIdFromToken error : {}", e.getMessage(), e);
			return null;
		}
	}

	private static Map<String, String> getUserDetailMap(JwtUserDetailDto jwtUserDetailDto) {
		Map<String, String> userDetailMap = new HashMap<>();

		if (ObjectUtils.isEmpty(jwtUserDetailDto)) {
			throw new MandatoryException();
		}

		userDetailMap.put("userId", jwtUserDetailDto.getUserId());
		userDetailMap.put("type", jwtUserDetailDto.getType());

		return userDetailMap;
	}
}
