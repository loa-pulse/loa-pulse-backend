# 로스트아크 API 응답 예시

실제 로스트아크 Open API에서 받은 응답 본문을 그대로 저장한 파일이다 (docs 04-testing.md: 추측으로 만든 외부 응답으로 테스트하지 않는다).

수집일: 2026-11-01

## 검색 (`POST /markets/items`)

모두 `"Sort": "GRADE", "SortCondition": "ASC"` (등급순, collector 수집 기준)로 요청했다.

| 파일 | `CategoryCode` | `PageNo` | 담긴 경우 |
|---|---|---|---|
| `search-refining-page1.json` | 50010 (재련 재료) | 1 | 꽉 찬 페이지(10개), 묶음 단위 100, `TradeRemainCount: null` |
| `search-engraving-page9.json` | 40000 (각인서) | 9 | 마지막 페이지(6개), 묶음 단위 1, `TradeRemainCount: 0`, 소수 평균가 |
| `search-refining-page99-empty.json` | 50010 (재련 재료) | 99 | 범위를 벗어난 페이지. `Items`가 빈 배열 |

## 함께 확인한 사실

- 응답 헤더: `X-RateLimit-Limit: 100`, `X-RateLimit-Remaining`(남은 횟수), `X-RateLimit-Reset`(Unix 초)
- 알 수 없는 `Sort` 값(예: `BOGUS`)은 오류 없이 등급순(`GRADE`)과 같은 결과를 돌려준다. 같은 등급 안에서는 `Id` 오름차순으로 보인다.
