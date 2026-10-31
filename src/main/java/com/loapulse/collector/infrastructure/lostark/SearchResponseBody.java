package com.loapulse.collector.infrastructure.lostark;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loapulse.collector.domain.MarketItemPrice;

/**
 * {@code POST /markets/items} 응답 본문. 필드 이름은 API 그대로(PascalCase)다.
 */
record SearchResponseBody(
		@JsonProperty("PageNo") int pageNo,
		@JsonProperty("TotalCount") int totalCount,
		@JsonProperty("Items") List<Item> items) {

	List<MarketItemPrice> toItemPrices() {
		if (items == null) {
			return List.of();
		}
		return items.stream().map(Item::toItemPrice).toList();
	}

	record Item(
			@JsonProperty("Id") long id,
			@JsonProperty("Name") String name,
			@JsonProperty("Grade") String grade,
			@JsonProperty("Icon") String icon,
			@JsonProperty("BundleCount") int bundleCount,
			@JsonProperty("CurrentMinPrice") int currentMinPrice,
			@JsonProperty("YDayAvgPrice") BigDecimal yesterdayAvgPrice,
			@JsonProperty("RecentPrice") int recentPrice,
			@JsonProperty("TradeRemainCount") Integer tradeRemainCount) {

		MarketItemPrice toItemPrice() {
			return new MarketItemPrice(id, name, grade, icon, bundleCount, currentMinPrice, yesterdayAvgPrice,
					recentPrice, tradeRemainCount);
		}

	}

}
