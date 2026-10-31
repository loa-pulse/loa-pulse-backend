package com.loapulse.collector.domain;

import java.util.Objects;
import java.util.Optional;

/**
 * 로스트아크 API 호출 실패. 수집기는 {@link Reason}에 따라 다르게 대응한다 (docs 03-operations).
 */
public class LostArkApiException extends RuntimeException {

	public enum Reason {

		/** 5xx, 타임아웃, 연결 실패. 기다리면 해결될 수 있어 수집 단위를 짧게 미뤄 재시도한다 */
		TRANSIENT,

		/** 429. 호출 한도를 넘었다. 한도가 다시 채워질 때까지 호출을 멈춘다 */
		RATE_LIMITED,

		/** 429를 뺀 4xx (잘못된 API 키, 잘못된 요청). 재시도해도 소용없어 설정이나 코드를 고쳐야 한다 */
		REJECTED

	}

	private final Reason reason;

	private final RateLimitStatus rateLimit;

	public LostArkApiException(Reason reason, String message, RateLimitStatus rateLimit, Throwable cause) {
		super(message, cause);
		this.reason = Objects.requireNonNull(reason, "reason");
		this.rateLimit = rateLimit;
	}

	public Reason reason() {
		return reason;
	}

	/**
	 * 실패 응답에 호출 한도 헤더가 있었다면 그 값. 429 응답의 헤더는 아직 실제로 확인하지 못해 없을 수 있다.
	 */
	public Optional<RateLimitStatus> rateLimit() {
		return Optional.ofNullable(rateLimit);
	}

}
