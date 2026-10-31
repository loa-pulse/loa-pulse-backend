package com.loapulse.collector.domain;

import java.time.Instant;
import java.util.Objects;

/**
 * 응답 헤더로 받은 호출 한도 정보 ({@code X-RateLimit-Limit}, {@code X-RateLimit-Remaining}, {@code X-RateLimit-Reset}).
 *
 * @param limit 구간당 호출 한도 (분당 100회)
 * @param remaining 이번 구간에 남은 호출 수
 * @param resetAt 호출 수가 다시 채워지는 시각
 */
public record RateLimitStatus(int limit, int remaining, Instant resetAt) {

	public RateLimitStatus {
		Objects.requireNonNull(resetAt, "resetAt");
	}

}
