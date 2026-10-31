package com.loapulse.collector.infrastructure.lostark;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.client.RestClient;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.loapulse.collector.domain.MarketItemPrice;
import com.loapulse.collector.domain.MarketSearchPage;
import com.loapulse.collector.domain.MarketSearchRequest;
import com.loapulse.collector.domain.RateLimitStatus;

/**
 * 응답 본문은 실제 API에서 받은 응답이다 (src/test/resources/lostark/README.md).
 */
class RestClientLostArkMarketClientTest {

	private static final String API_KEY = "test-api-key";

	@RegisterExtension
	static WireMockExtension wireMock = WireMockExtension.newInstance()
			.options(wireMockConfig().dynamicPort())
			.build();

	private RestClientLostArkMarketClient client;

	@BeforeEach
	void setUp() {
		LostArkApiProperties properties = new LostArkApiProperties(URI.create(wireMock.baseUrl()), API_KEY,
				Duration.ofSeconds(1), Duration.ofSeconds(1));
		RestClient restClient = new LostArkApiConfig().lostArkRestClient(RestClient.builder(), properties);
		client = new RestClientLostArkMarketClient(restClient);
	}

	@Test
	void 등급순_정렬과_인증_헤더로_검색을_요청한다() {
		stubSearch("search-refining-page1.json", 99);

		client.search(new MarketSearchRequest(50010, 1));

		wireMock.verify(postRequestedFor(urlEqualTo("/markets/items"))
				.withHeader("authorization", equalTo("bearer " + API_KEY))
				.withHeader("Content-Type", equalTo("application/json"))
				.withRequestBody(equalToJson("""
						{ "Sort": "GRADE", "CategoryCode": 50010, "PageNo": 1, "SortCondition": "ASC" }
						""")));
	}

	@Test
	void 거래_횟수_제한이_없는_아이템의_시세를_변환한다() {
		stubSearch("search-refining-page1.json", 99);

		MarketSearchPage page = client.search(new MarketSearchRequest(50010, 1));

		assertThat(page.pageNo()).isEqualTo(1);
		assertThat(page.totalCount()).isEqualTo(33);
		assertThat(page.items()).hasSize(10);
		assertThat(page.items().getFirst()).isEqualTo(new MarketItemPrice(66102003L, "파괴석 결정", "일반",
				"https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_6_105.png", 100, 1,
				new BigDecimal("1.0"), 1, null));
	}

	@Test
	void 마지막_페이지와_거래_가능_횟수와_소수_평균가를_변환한다() {
		stubSearch("search-engraving-page9.json", 99);

		MarketSearchPage page = client.search(new MarketSearchRequest(40000, 9));

		assertThat(page.totalCount()).isEqualTo(86);
		assertThat(page.items()).hasSize(6);
		MarketItemPrice first = page.items().getFirst();
		assertThat(first.id()).isEqualTo(65203005L);
		assertThat(first.bundleCount()).isEqualTo(1);
		assertThat(first.currentMinPrice()).isEqualTo(40998);
		assertThat(first.yesterdayAvgPrice()).isEqualByComparingTo("41278.3");
		assertThat(first.recentPrice()).isEqualTo(41000);
		assertThat(first.tradeRemainCount()).isZero();
	}

	@Test
	void 범위를_벗어난_페이지는_빈_목록이다() {
		stubSearch("search-refining-page99-empty.json", 99);

		MarketSearchPage page = client.search(new MarketSearchRequest(50010, 99));

		assertThat(page.totalCount()).isEqualTo(33);
		assertThat(page.items()).isEmpty();
	}

	@Test
	void 응답_헤더의_호출_한도를_함께_돌려준다() {
		stubSearch("search-refining-page1.json", 57);

		MarketSearchPage page = client.search(new MarketSearchRequest(50010, 1));

		assertThat(page.rateLimit())
				.isEqualTo(new RateLimitStatus(100, 57, Instant.ofEpochSecond(1793475482L)));
	}

	private static void stubSearch(String fixture, int remaining) {
		wireMock.stubFor(post(urlEqualTo("/markets/items")).willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json; charset=utf-8")
				.withHeader("X-RateLimit-Limit", "100")
				.withHeader("X-RateLimit-Remaining", String.valueOf(remaining))
				.withHeader("X-RateLimit-Reset", "1793475482")
				.withBody(fixture(fixture))));
	}

	private static String fixture(String name) {
		try (InputStream in = RestClientLostArkMarketClientTest.class.getResourceAsStream("/lostark/" + name)) {
			if (in == null) {
				throw new IllegalArgumentException("응답 예시가 없다: " + name);
			}
			return new String(in.readAllBytes(), StandardCharsets.UTF_8);
		}
		catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

}
