package com.loapulse.collector.infrastructure.lostark;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.loapulse.collector.domain.LostArkApiException;
import com.loapulse.collector.domain.LostArkApiException.Reason;
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
		ResponseEntity<SearchResponseBody> response;
		try {
			response = restClient.post()
					.uri(SEARCH_PATH)
					.contentType(MediaType.APPLICATION_JSON)
					.body(SearchRequestBody.byGrade(request))
					.retrieve()
					.toEntity(SearchResponseBody.class);
		}
		catch (HttpClientErrorException.TooManyRequests ex) {
			throw failure(Reason.RATE_LIMITED, request, ex);
		}
		catch (HttpClientErrorException ex) {
			throw failure(Reason.REJECTED, request, ex);
		}
		catch (HttpServerErrorException ex) {
			throw failure(Reason.TRANSIENT, request, ex);
		}
		catch (ResourceAccessException ex) {
			// 타임아웃과 연결 실패
			throw new LostArkApiException(Reason.TRANSIENT, "로스트아크 API에 접근하지 못했다: " + request, null, ex);
		}

		SearchResponseBody body = response.getBody();
		if (body == null) {
			throw new IllegalStateException("검색 응답 본문이 비어 있다: " + request);
		}
		return new MarketSearchPage(body.pageNo(), body.totalCount(), body.toItemPrices(),
				RateLimitHeaders.parse(response.getHeaders()));
	}

	private static LostArkApiException failure(Reason reason, MarketSearchRequest request,
			HttpStatusCodeException ex) {
		String message = "로스트아크 API가 " + ex.getStatusCode().value() + "로 응답했다: " + request;
		return new LostArkApiException(reason, message, RateLimitHeaders.tryParse(ex.getResponseHeaders()).orElse(null),
				ex);
	}

}
