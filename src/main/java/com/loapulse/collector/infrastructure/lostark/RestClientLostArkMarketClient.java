package com.loapulse.collector.infrastructure.lostark;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.loapulse.collector.domain.LostArkMarketClient;
import com.loapulse.collector.domain.MarketSearchPage;
import com.loapulse.collector.domain.MarketSearchRequest;

/**
 * {@link LostArkMarketClient}의 HTTP 구현. 주소, 인증, 타임아웃은 {@link LostArkApiConfig}가 정한다.
 */
@Component
class RestClientLostArkMarketClient implements LostArkMarketClient {

	private static final String SEARCH_PATH = "/markets/items";

	private final RestClient restClient;

	RestClientLostArkMarketClient(@Qualifier("lostArkRestClient") RestClient restClient) {
		this.restClient = restClient;
	}

	@Override
	public MarketSearchPage search(MarketSearchRequest request) {
		ResponseEntity<SearchResponseBody> response = restClient.post()
				.uri(SEARCH_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.body(SearchRequestBody.byGrade(request))
				.retrieve()
				.toEntity(SearchResponseBody.class);

		SearchResponseBody body = response.getBody();
		if (body == null) {
			throw new IllegalStateException("검색 응답 본문이 비어 있다: " + request);
		}
		return new MarketSearchPage(body.pageNo(), body.totalCount(), body.toItemPrices(),
				RateLimitHeaders.parse(response.getHeaders()));
	}

}
