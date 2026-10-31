package com.loapulse.collector.domain;

import java.util.List;
import java.util.Objects;

/**
 * 카테고리 검색 페이지 하나의 결과.
 *
 * @param pageNo 응답한 페이지 번호
 * @param totalCount 카테고리 전체 아이템 수. 페이지 목록을 만들 때 쓴다
 * @param items 이 페이지의 아이템 시세. 범위를 벗어난 페이지면 비어 있다
 * @param rateLimit 응답 헤더의 호출 한도 정보
 */
public record MarketSearchPage(int pageNo, int totalCount, List<MarketItemPrice> items, RateLimitStatus rateLimit) {

	public MarketSearchPage {
		items = List.copyOf(items);
		Objects.requireNonNull(rateLimit, "rateLimit");
	}

}
