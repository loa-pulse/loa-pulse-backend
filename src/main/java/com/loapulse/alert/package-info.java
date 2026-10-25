/**
 * alert 모듈 (핵심 도메인). 목표가·급변동 알림의 조건 평가, 발송, 상태 관리를 맡는다.
 * <p>
 * collector, market, user에 의존한다. 다른 모듈은 {@code alert.api}만 참조한다.
 */
package com.loapulse.alert;
