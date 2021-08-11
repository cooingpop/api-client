package com.cooingpop.apiclient.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그아웃한 토큰 관리하는 클래스
 * (대체 가능 > Redis 이용)
 * @author 박준영
 **/

@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtTokenBlockUtil {
	public static Map<String, Object> tokenBlockList = new HashMap<>();

	public static boolean isBlockToken(String token) {
		return tokenBlockList.containsKey(token);
	}

	public static boolean insertBlockToken(String token) {
		try {
			tokenBlockList.put(token, LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond());

			return true;
		} catch (Exception ex) {
			log.error("insertBlockToken exception " + ex.getMessage(), ex);
			return false;
		}
	}
}
