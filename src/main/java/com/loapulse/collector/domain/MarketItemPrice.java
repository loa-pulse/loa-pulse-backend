package com.loapulse.collector.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 검색 결과에 담긴 아이템 하나의 시세. 가격은 모두 {@code bundleCount}개 묶음 기준이다 (docs 02-data-model).
 *
 * @param id 아이템 ID. 시세의 식별자는 이 값 하나다
 * @param name 아이템 이름
 * @param grade 등급 (예: 일반, 유물)
 * @param icon 아이콘 URL
 * @param bundleCount 묶음 단위
 * @param currentMinPrice 현재 최저가 (골드, 정수)
 * @param yesterdayAvgPrice 전일 평균가 (골드, 소수 첫째 자리까지)
 * @param recentPrice 최근 거래가 (골드, 정수)
 * @param tradeRemainCount 남은 거래 가능 횟수. 제한이 없는 아이템은 {@code null}. 상세 조회에서 쓸 항목을 고르는 데만 쓴다
 */
public record MarketItemPrice(
		long id,
		String name,
		String grade,
		String icon,
		int bundleCount,
		int currentMinPrice,
		BigDecimal yesterdayAvgPrice,
		int recentPrice,
		Integer tradeRemainCount) {

	public MarketItemPrice {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(grade, "grade");
		Objects.requireNonNull(yesterdayAvgPrice, "yesterdayAvgPrice");
	}

}
