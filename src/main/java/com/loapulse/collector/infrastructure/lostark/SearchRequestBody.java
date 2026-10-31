package com.loapulse.collector.infrastructure.lostark;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loapulse.collector.domain.MarketSearchRequest;

/**
 * {@code POST /markets/items} 요청 본문.
 * <p>
 * 정렬은 항상 등급순이다. 같은 등급 안에서는 {@code Id} 순이라 가격이 바뀌어도 페이지 구성이 흔들리지 않는다.
 * API는 모르는 정렬 값에 오류를 내지 않고 등급순으로 처리하므로, 값이 바뀌지 않았는지 테스트로 지킨다.
 */
record SearchRequestBody(
		@JsonProperty("Sort") String sort,
		@JsonProperty("CategoryCode") int categoryCode,
		@JsonProperty("PageNo") int pageNo,
		@JsonProperty("SortCondition") String sortCondition) {

	static SearchRequestBody byGrade(MarketSearchRequest request) {
		return new SearchRequestBody("GRADE", request.categoryCode(), request.pageNo(), "ASC");
	}

}
