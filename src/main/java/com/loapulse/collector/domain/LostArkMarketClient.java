package com.loapulse.collector.domain;

/**
 * 로스트아크 거래소 시세를 가져오는 내부 포트. 구현은 collector의 infrastructure에 둔다.
 * <p>
 * 결과에는 응답 헤더의 호출 한도 정보가 함께 담긴다. {@code CallBudget}이 이 값으로 남은 호출 수를 보정한다.
 */
public interface LostArkMarketClient {

	/**
	 * 카테고리 검색 페이지 하나를 등급순으로 가져온다 (수집 단위, docs D26).
	 *
	 * @throws LostArkApiException 호출이 실패했을 때. 원인은 {@link LostArkApiException#reason()}으로 구분한다
	 */
	MarketSearchPage search(MarketSearchRequest request);

}
