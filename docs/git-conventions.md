# Git 운영 규칙

> 이슈 → 브랜치 → 커밋 → PR → 머지 커밋. 근거는 [D25](https://github.com/loa-pulse/loa-pulse-docs/blob/main/design/decisions.md)다.

## 흐름

```
1. 이슈 발행        무엇을 왜 할지, 완료 기준
2. 브랜치 생성      main에서 <종류>/<이슈 번호>-<짧은 설명>
3. 커밋             하나의 책임, 파일 5개 이하
4. PR 생성          Closes #<이슈 번호>, CI 통과
5. 머지 커밋        Create a merge commit. 브랜치는 자동 삭제
```

- `main`은 항상 배포할 수 있는 상태다. `main`에 직접 커밋하거나 push하지 않는다.
- 이슈 없이 브랜치를 만들지 않는다. 오타 수정처럼 아주 작은 변경도 이슈를 먼저 만든다.

## 1. 이슈

- **제목**: 무엇을 할지 한 문장으로 쓴다. 예: `로스트아크 검색 API 클라이언트 구현`
- **본문**

  ```markdown
  ## 배경
  왜 필요한지. 관련 설계 문서나 결정 번호(Dxx) 링크

  ## 할 일
  - [ ] 커밋 단위로 나눈 작업 목록

  ## 완료 기준
  - 무엇이 되면 닫을 수 있는지
  ```

- **라벨**: `enhancement`(기능), `bug`(버그), `documentation`(문서) 중 하나를 붙인다.
- **마일스톤**: 해당하는 마일스톤(M0~M6)에 연결한다.
- 한 이슈는 PR 하나로 닫을 수 있는 크기로 잡는다. 크면 이슈를 나눈다.

```bash
gh issue create --title "로스트아크 검색 API 클라이언트 구현" --label enhancement --milestone M1
```

## 2. 브랜치

`<종류>/<이슈 번호>-<짧은 설명>` (설명은 영어 소문자와 `-`)

| 종류 | 용도 | 예 |
|---|---|---|
| `feat` | 기능 추가 | `feat/12-lostark-market-client` |
| `fix` | 버그 수정 | `fix/18-rate-limit-header-parse` |
| `refactor` | 동작을 바꾸지 않는 구조 개선 | `refactor/21-split-collection-planner` |
| `chore` | 빌드, 의존성, 설정, CI | `chore/5-add-wiremock` |
| `docs` | 문서 | `docs/3-git-conventions` |

이슈와 연결된 브랜치는 `gh issue develop`으로 만든다. 이슈 화면의 Development 항목에 브랜치가 표시된다.

```bash
gh issue develop 12 --name feat/12-lostark-market-client --base main --checkout
```

## 3. 커밋

### 메시지

```
<종류>: <무엇을 했는지 한글로 요약>
```

| 종류 | 용도 |
|---|---|
| `feat` | 기능 추가 |
| `fix` | 버그 수정 |
| `refactor` | 동작을 바꾸지 않는 구조 개선 |
| `test` | 테스트, 테스트용 데이터 |
| `docs` | 문서 |
| `chore` | 빌드, 의존성, 설정 |
| `ci` | CI 워크플로 |

- 요약은 "~ 추가", "~ 구현", "~ 수정"처럼 명사형으로 끝낸다. 예: `feat: 로스트아크 검색 API 어댑터 구현`
- 이유가 제목만으로 드러나지 않으면 본문에 적는다. 결정 번호가 있으면 `(Dxx)`를 붙인다.

### 단위

- 커밋 하나는 **하나의 책임**만 다룬다 (예: 인터페이스 정의 → 구현체 → 연결).
- 커밋 하나에 **파일 5개 이하**. 넘으면 책임별로 나눈다.
- 각 커밋에서 빌드와 테스트가 통과해야 한다.

## 4. PR

- **제목**: 이슈 제목과 같은 내용을 커밋 메시지 형식으로 쓴다. 예: `feat: 로스트아크 검색 API 클라이언트 구현`
- **본문**

  ```markdown
  Closes #12

  ## 변경 내용
  - 커밋 단위로 무엇을 왜 바꿨는지

  ## 관련 결정
  - Dxx (없으면 생략)

  ## 확인
  - 테스트, 측정 결과, 화면 캡처

  ## 관련 PR
  - 다른 레포에 영향이 있으면 상대 PR 링크 (없으면 생략)
  ```

- `Closes #<번호>`로 이슈를 연결해 머지하면 이슈가 자동으로 닫히게 한다.
- 마일스톤은 이슈와 같은 것으로 연결한다.
- CI가 통과해야 머지한다.

```bash
gh pr create --base main --title "feat: 로스트아크 검색 API 클라이언트 구현" --milestone M1 --body-file pr.md
```

## 5. 머지

- **머지 커밋만 쓴다.** squash와 rebase merge는 레포 설정에서 꺼져 있다.
- 머지 커밋은 작은 커밋들을 그대로 보존하면서 PR 하나를 커밋 하나로 묶는다. PR 단위 이력은 `git log --first-parent main`으로 본다.
- 머지한 브랜치는 원격에서 자동으로 지워진다. 로컬 브랜치는 직접 정리한다.

```bash
gh pr merge 13 --merge
git switch main && git pull && git branch -d feat/12-lostark-market-client
```

## 마일스톤과 태그

- GitHub Milestone `M0`~`M6`에 이슈와 PR을 연결한다.
- 마일스톤을 마치면 `main`에 태그를 붙인다. M1 → `v0.1.0`, M2 → `v0.2.0`.

## 레포 사이 연결

- 다른 레포(docs, backend, frontend)에 영향을 주는 PR은 상대 PR 링크를 건다.
- 설계 결정에 따른 변경이면 docs의 결정 번호(Dxx)를 적는다.
