/**
 * collector 모듈 (핵심 도메인). 무엇을 언제 수집할지 결정하고 로스트아크 API를 호출한다 (등급제, 호출 예산).
 * <p>
 * 가장 위 모듈이라 다른 업무 모듈에 의존하지 않는다. 다른 모듈은 {@code collector.api}만 참조한다.
 */
package com.loapulse.collector;
