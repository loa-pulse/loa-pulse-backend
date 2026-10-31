# AGENTS.md

LoaPulse 백엔드 저장소의 문서 지도다. 작업 전에 필요한 문서를 찾아 읽는다.

## 이 저장소

| 문서 | 내용 |
|---|---|
| [README.md](README.md) | 기술 스택, 실행과 테스트 방법, 모듈 구성 |
| [docs/git-conventions.md](docs/git-conventions.md) | 이슈, 브랜치, 커밋, PR, 머지 규칙 |
| [src/test/resources/lostark/README.md](src/test/resources/lostark/README.md) | 테스트에 쓰는 로스트아크 API 실제 응답 예시 |

## 기획·설계 (loa-pulse-docs)

[loa-pulse-docs](https://github.com/loa-pulse/loa-pulse-docs) 저장소에 있다. 로컬에서는 `../docs`다.

| 문서 | 내용 |
|---|---|
| `planning/02-features.md` | 기능 정의와 우선순위 |
| `planning/04-policies.md` | 서비스 정책 (신선도 목표, 알림 정책) |
| `planning/05-milestones.md` | 마일스톤과 완료 기준 |
| `planning/07-api-findings.md` | 로스트아크 Open API 조사 결과 |
| `design/00-architecture.md` | 아키텍처, 모듈 경계, 패키지 구조 |
| `design/01-data-flow.md` | 데이터 흐름과 이벤트 |
| `design/02-data-model.md` | 데이터 모델과 시계열 보관 정책 |
| `design/03-operations.md` | 장애 처리와 관측성 |
| `design/04-testing.md` | 테스트 전략 |
| `design/decisions.md` | 기술 결정 기록 (Dxx) |
| `design/modules/<모듈>.md` | 모듈별 설계: collector, market, realtime, alert, user |
