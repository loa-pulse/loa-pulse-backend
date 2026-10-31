package com.loapulse.collector.infrastructure.lostark;

import java.time.Instant;

import org.springframework.http.HttpHeaders;

import com.loapulse.collector.domain.RateLimitStatus;

/**
 * 응답 헤더에서 호출 한도 정보를 읽는다 (docs 07-api-findings: 인증과 호출 제한).
 */
final class RateLimitHeaders {

	static final String LIMIT = "X-RateLimit-Limit";
	static final String REMAINING = "X-RateLimit-Remaining";
	static final String RESET = "X-RateLimit-Reset";

	private RateLimitHeaders() {
	}

	/**
	 * @throws IllegalStateException 헤더가 없거나 숫자가 아닐 때
	 */
	static RateLimitStatus parse(HttpHeaders headers) {
		int limit = Math.toIntExact(required(headers, LIMIT));
		int remaining = Math.toIntExact(required(headers, REMAINING));
		Instant resetAt = Instant.ofEpochSecond(required(headers, RESET));
		return new RateLimitStatus(limit, remaining, resetAt);
	}

	private static long required(HttpHeaders headers, String name) {
		String value = headers.getFirst(name);
		if (value == null) {
			throw new IllegalStateException("응답에 " + name + " 헤더가 없다");
		}
		try {
			return Long.parseLong(value.trim());
		}
		catch (NumberFormatException ex) {
			throw new IllegalStateException(name + " 헤더가 숫자가 아니다: " + value, ex);
		}
	}

}
