# LoaPulse Backend

> 로스트아크 거래소 시세를 실시간으로 추적하고, 원하는 가격이 되면 디스코드로 알려주는 서비스의 백엔드

기획과 설계는 [loa-pulse-docs](https://github.com/loa-pulse/loa-pulse-docs)에 있다.

## 기술 스택

Java 21, Spring Boot 4.1, Gradle (Kotlin DSL), PostgreSQL, Redis, Flyway, Testcontainers, ArchUnit

## 실행

필요한 것: JDK 21, Docker

```bash
# 1. 로컬 PostgreSQL(15432)과 Redis(16379) 실행
docker compose up -d

# 2. 애플리케이션 실행
./gradlew bootRun
```

compose 없이 Testcontainers로 DB와 Redis를 함께 띄우려면 `./gradlew bootTestRun`을 쓴다.

접속 정보는 환경 변수로 바꿀 수 있다: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `REDIS_HOST`, `REDIS_PORT`

## 테스트

```bash
./gradlew test
```

통합 테스트는 Testcontainers로 실제 PostgreSQL과 Redis를 띄우므로 Docker가 실행 중이어야 한다.

## 모듈 구성

모듈러 모놀리스이고, 모듈 경계는 `ArchitectureTest`(ArchUnit)로 검사한다.
자세한 규칙은 [아키텍처 개요](https://github.com/loa-pulse/loa-pulse-docs/blob/main/design/00-architecture.md)를 따른다.

| 패키지 | 역할 |
|---|---|
| `collector` | 무엇을 언제 수집할지 결정하고 로스트아크 API를 호출 |
| `market` | 시세 저장, 조회, 롤업, 급등락 랭킹 |
| `alert` | 목표가·급변동 알림의 조건 평가, 발송, 상태 관리 |
| `user` | 디스코드 로그인, 프로필, 관심 아이템 |
| `realtime` | SSE 연결 관리, 인스턴스 간 전달 |
| `shared` | 식별자와 값 객체 |

## 브랜치 전략

`main`과 작업 브랜치(`feat/...`, `fix/...`)로 운영하고, PR은 머지 커밋으로 합친다 ([D25](https://github.com/loa-pulse/loa-pulse-docs/blob/main/design/decisions.md)).
