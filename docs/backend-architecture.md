# Pass-IT Backend Architecture

## 1. Chosen Strategy

Pass-IT backend will start as a modular monolith.

- One Spring Boot application
- One MySQL database
- MyBatis for persistence
- Internal `judge` module for code execution and result calculation
- Docker-based sandbox runner for Python, Java, and C++

This approach is chosen because it is fast to build, easy to debug, and can later be split into separate services if needed.

---

## 2. High-Level Modules

### `user`
- 회원 정보 조회
- 로그인/권한과 연결될 사용자 도메인

### `problem`
- 문제 목록 조회
- 문제 상세 조회
- 코딩 문제 등록/수정
- 샘플 테스트케이스 관리

### `submission`
- 코드 제출 생성
- 제출 결과 조회
- 내 제출 이력 조회

### `judge`
- 제출 채점 시작
- 언어별 코드 실행
- 테스트케이스 반복 실행
- 최종 판정 계산

### `community`
- 게시글, 댓글 기능

### `common`
- 공통 응답 포맷
- 예외 처리
- enum, 유틸

---

## 3. Package Structure

```text
com.ssafy.passit
├─ common
│  ├─ config
│  ├─ exception
│  ├─ response
│  └─ type
├─ user
│  ├─ controller
│  ├─ dto
│  ├─ mapper
│  ├─ model
│  └─ service
├─ problem
│  ├─ controller
│  ├─ dto
│  ├─ mapper
│  ├─ model
│  └─ service
├─ submission
│  ├─ controller
│  ├─ dto
│  ├─ mapper
│  ├─ model
│  └─ service
├─ judge
│  ├─ dto
│  ├─ runner
│  ├─ service
│  └─ support
└─ community
   ├─ controller
   ├─ dto
   ├─ mapper
   ├─ model
   └─ service
```

Notes:

- `model`: DB row와 거의 1:1 대응되는 객체
- `dto`: 요청/응답 객체
- `mapper`: MyBatis interface
- `service`: 비즈니스 로직
- `runner`: 언어별 채점 실행기

---

## 4. Domain Design

### `problems`
- 공통 문제 메타 저장
- `problem_type`으로 `QUIZ`, `CODING` 구분
- 코딩 문제는 `input_description`, `output_description`, `constraint_text` 사용

### `test_cases`
- 코딩 문제의 입력/정답 저장
- `is_sample = true`: 화면에 공개
- `is_sample = false`: 숨은 채점용

### `submissions`
- 사용자의 코드 제출 저장
- 채점 상태와 최종 판정 저장

Recommended enums:

- `ProblemType`: `QUIZ`, `CODING`
- `ProblemSourceType`: `AI`, `USER`, `EXAM`
- `ProblemStatus`: `PENDING`, `APPROVED`, `REJECTED`
- `LanguageType`: `PYTHON`, `JAVA`, `CPP`
- `SubmissionStatus`: `PENDING`, `RUNNING`, `DONE`, `FAILED`
- `VerdictType`: `AC`, `WA`, `CE`, `RE`, `TLE`, `MLE`

---

## 5. Request Flow

### Problem Detail
1. Client requests a problem detail.
2. `ProblemService` loads problem metadata.
3. If the problem is `CODING`, sample test cases are loaded together.
4. Hidden test cases are never returned to normal users.

### Code Submission
1. Client sends `problemId`, `language`, `sourceCode`.
2. `SubmissionService` validates the request.
3. Submission row is inserted with `PENDING`.
4. `JudgeService` starts judging.
5. `JudgeService` updates submission to `RUNNING`.
6. Hidden test cases are loaded.
7. Runner executes the code against each test case.
8. Final verdict is calculated.
9. Submission row is updated to `DONE` or `FAILED`.
10. Client reads the result through submission APIs.

---

## 6. Judge Module Design

### Core Services

#### `JudgeService`
- 채점 전체 오케스트레이션
- 문제 제한 시간, 메모리 제한 조회
- 테스트케이스 로드
- 실행기 호출
- 판정 종합

#### `CodeRunner`
- 언어별 실행 인터페이스

```java
public interface CodeRunner {
    LanguageType supportLanguage();
    ExecutionResult run(ExecutionRequest request);
}
```

Implementations:

- `PythonCodeRunner`
- `JavaCodeRunner`
- `CppCodeRunner`

### Docker Execution Rules

- Each submission runs in an isolated container
- Input is passed through stdin
- Output is read from stdout
- Use temporary working directory for source file
- No network access
- Limited CPU and memory
- Auto-remove container after execution

Example source file policy:

- Python: `main.py`
- Java: `Main.java`
- C++: `main.cpp`

---

## 7. Initial API Scope

### Problem APIs
- `GET /api/problems`
- `GET /api/problems/{problemId}`
- `POST /api/problems`
- `PUT /api/problems/{problemId}`

### Submission APIs
- `POST /api/problems/{problemId}/submissions`
- `GET /api/submissions/{submissionId}`
- `GET /api/users/{userId}/submissions`
- `GET /api/problems/{problemId}/submissions`

### Admin APIs
- `POST /api/problems/{problemId}/test-cases`
- `GET /api/admin/problems/{problemId}/test-cases`

For MVP, do not build every API at once.
Start with:

- `GET /api/problems`
- `GET /api/problems/{problemId}`
- `POST /api/problems/{problemId}/submissions`
- `GET /api/submissions/{submissionId}`

---

## 8. MyBatis Design Direction

### Mapper Split

- `ProblemMapper`
- `TestCaseMapper`
- `SubmissionMapper`

### Query Style

- 목록 조회: 필요한 컬럼만 조회
- 상세 조회: 문제 메타 + 샘플 테스트케이스 분리 조회
- 제출 저장: `useGeneratedKeys`
- 상태 변경: 명확한 update 쿼리 분리

Recommended mapper methods:

- `ProblemMapper.findAllApprovedCodingProblems()`
- `ProblemMapper.findProblemById(Long problemId)`
- `TestCaseMapper.findSampleTestCases(Long problemId)`
- `TestCaseMapper.findHiddenTestCases(Long problemId)`
- `SubmissionMapper.insertSubmission(Submission submission)`
- `SubmissionMapper.findSubmissionById(Long submissionId)`
- `SubmissionMapper.updateSubmissionRunning(Long submissionId)`
- `SubmissionMapper.updateSubmissionResult(Submission submission)`

---

## 9. Implementation Phases

### Phase 1: Foundation
- 공통 예외 처리
- enum 정리
- problem, submission 기본 패키지 생성
- MyBatis mapper xml 구조 생성

### Phase 2: Problem Read APIs
- 문제 목록 조회
- 문제 상세 조회
- 샘플 테스트케이스 조회

### Phase 3: Submission APIs
- 코드 제출 저장
- 제출 단건 조회

### Phase 4: Judge MVP
- Python runner 먼저 구현
- stdin/stdout 비교
- `AC`, `WA`, `CE`, `RE`, `TLE` 정도만 우선 지원

### Phase 5: Language Expansion
- Java runner 추가
- C++ runner 추가

### Phase 6: Stabilization
- 예외 메시지 정리
- Docker 실행 실패 대응
- 운영 로그 보강

---

## 10. Recommended First Build Order

Build in this order:

1. Common exception/response/enums
2. Problem read API
3. Submission create/read API
4. Python judge MVP
5. Java judge
6. C++ judge

This order reduces risk and makes it possible to verify the end-to-end flow early.
