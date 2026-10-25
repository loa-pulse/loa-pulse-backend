/**
 * realtime 모듈 (기술 지원). SSE 연결 관리와 인스턴스 간 전달을 맡는다.
 * <p>
 * 가장 아래 모듈이라 collector, market, alert에 의존하고, 아무 모듈도 realtime에 의존하지 않는다.
 */
package com.loapulse.realtime;
