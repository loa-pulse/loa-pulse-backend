package com.loapulse.collector.infrastructure.lostark;

import java.net.URI;
import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 로스트아크 Open API 접속 설정 ({@code loapulse.lostark.*}).
 *
 * @param baseUrl API 주소
 * @param apiKey API 키. 환경 변수 {@code LOSTARK_API_KEY}로만 받고 저장소에 두지 않는다
 * @param connectTimeout 연결 타임아웃
 * @param readTimeout 응답 대기 타임아웃. 넘으면 일시 오류로 보고 수집 단위를 짧게 미뤄 재시도한다
 */
@ConfigurationProperties("loapulse.lostark")
public record LostArkApiProperties(URI baseUrl, String apiKey, Duration connectTimeout, Duration readTimeout) {
}
