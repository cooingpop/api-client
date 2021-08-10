/*
 * @(#) JwtTokenProvider.java 2021. 08. 08.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.cooingpop.apiclient.api.member.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 박준영
 **/
@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenUtil {
	private static String SECRET_KEY = "SeCrEtKeY-CoOiNgPoP-SeCrEtKeY-JwtToken";
	private static long ACCESS_TOKEN_VALIDATION = 60 * 60 * 1000;
	private static long REFRESH_TOKEN_VALIDATION = 60 * 60 * 24 * 30 * 1000;
	private final UserDetailsService userDetailsService;
	// @PostConstruct
	// protected void init() {
	// 	SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
	// }

	// access token 생성
	public static String generateAccessToken(User user) {
		return generateToken(user, ACCESS_TOKEN_VALIDATION);
	}

	// refresh token 생성
	public static String generateRefreshToken(User user) {
		return generateToken(user, REFRESH_TOKEN_VALIDATION);
	}

	/**
	 * 토큰 생성
	 * 1. 토큰, issuer, expiration, subject, id 등 claims 정의
	 * 2. HS512 알고리즘과 secret key를 가지고 JWT 서명
	 * 3. 문자열 압축
	 * @param user
	 * @param expireTime
	 * @return
	 */
	public static String generateToken(User user, Long expireTime) {
		Date now = new Date();

		if (expireTime == null) {
			expireTime = createExpireTime();
		}

		return Jwts.builder()
			.setSubject(user.getName())
			.setHeader(createHeader())
			.setClaims(createClaims(user))
			.setIssuedAt(now) // 토큰 발행일자
			.setExpiration(new Date(now.getTime() + expireTime))
			.signWith(getSigninKey(SECRET_KEY), SignatureAlgorithm.HS256) //  secret, 암호화 알고리즘
			.compact();
	}

	private static Map<String, Object> createHeader() {
		Map<String, Object> header = new HashMap<>();
		header.put("typ", "JWT");
		header.put("alg", "HS256");
		header.put("regDate", System.currentTimeMillis());

		return header;
	}

	private static Map<String, Object> createClaims(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("name", user.getName());
		claims.put("email", user.getEmail());
		claims.put("role", user.getRole());

		return claims;
	}

	private static long createExpireTime() {
		LocalDateTime localDateTime = LocalDateTime.now().plusHours(1);

		return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	public static Claims getClaimsFormToken(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(getSigninKey(SECRET_KEY))
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
			log.error(e.getMessage(), e);
			throw new InvalidParameterException("유효하지 않은 토큰입니다");
		}
	}

	private static String getUserEmailFromToken(String token) {
		Claims claims = getClaimsFormToken(token);

		return (String)claims.get("email");
	}

	private static String getRoleFromToken(String token) {
		Claims claims = getClaimsFormToken(token);

		return claims.get("role").toString();
	}

	public static String getUserNameFromToken(String token) {
		Claims claims = getClaimsFormToken(token);

		return claims.get("name").toString();
	}

	// 인증 성공시 SecurityContextHolder에 저장할 Authentication 객체 생성
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserNameFromToken(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	private static Key getSigninKey(String secretKey) {
		byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public static String getTokenFromHeader(String header) {
		return header.split(" ")[1];
	}

	public static String resolveToken(HttpServletRequest req) {
		return req.getHeader("X-AUTH-TOKEN");
	}

	public static boolean validToken(String token) {
		try {
			Claims claims = getClaimsFormToken(token);
			log.info("expireTime :" + claims.getExpiration());
			log.info("email :" + claims.get("email"));
			log.info("role :" + claims.get("role"));
			return !claims.getExpiration().before(new Date());
		} catch (ExpiredJwtException exception) {
			log.error("Token Expired");
			return false;
		} catch (JwtException exception) {
			log.error("Token Exception");
			return false;
		} catch (NullPointerException exception) {
			log.error("Token is null");
			return false;
		}
	}
}
