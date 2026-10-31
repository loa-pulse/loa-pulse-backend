package com.loapulse.collector.domain;

/**
 * 카테고리 검색 페이지 하나의 요청. 정렬은 페이지 구성이 흔들리지 않도록 항상 등급순이라 따로 받지 않는다.
 *
 * @param categoryCode 거래소 카테고리 코드 (예: 50010 재련 재료)
 * @param pageNo 1부터 시작하는 페이지 번호
 */
public record MarketSearchRequest(int categoryCode, int pageNo) {

	public MarketSearchRequest {
		if (categoryCode <= 0) {
			throw new IllegalArgumentException("카테고리 코드는 양수여야 한다: " + categoryCode);
		}
		if (pageNo < 1) {
			throw new IllegalArgumentException("페이지 번호는 1부터 시작한다: " + pageNo);
		}
	}

}
