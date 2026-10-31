package com.loapulse.collector.infrastructure.lostark;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

/**
 * 로스트아크 API 전용 {@link RestClient}. 주소, 인증 헤더, 타임아웃을 여기서 한 번만 정한다.
 * <p>
 * 스프링이 제공하는 {@link RestClient.Builder}에서 시작해 호출 지표(Micrometer)가 그대로 붙는다.
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(LostArkApiProperties.class)
class LostArkApiConfig {

	@Bean
	RestClient lostArkRestClient(RestClient.Builder builder, LostArkApiProperties properties) {
		HttpClientSettings settings = HttpClientSettings.defaults()
				.withTimeouts(properties.connectTimeout(), properties.readTimeout());
		return builder.clone()
				.baseUrl(properties.baseUrl())
				.requestFactory(ClientHttpRequestFactoryBuilder.jdk().build(settings))
				.defaultHeader(HttpHeaders.AUTHORIZATION, "bearer " + properties.apiKey())
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

}
