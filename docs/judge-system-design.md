# Pass-IT 채점 시스템 설계

## 1. 목표

이 문서는 **1차 MVP 채점 시스템만** 다룹니다.

현재 범위:

- 사용자 코드 제출 받기
- 코딩 문제의 숨은 테스트케이스 조회
- Docker 안에서 코드 실행
- `stdin/stdout` 비교
- 최종 결과를 `submissions` 테이블에 저장

현재 범위에서 제외:

- 문제 생성 기능
- 문제 자동 검수 파이프라인
- 큐 기반 비동기 워커
- 테스트케이스별 상세 결과 테이블
- 멀티 파일 프로젝트 실행

---

## 2. MVP 요구사항

### 지원 언어
- `PYTHON`
- `JAVA`
- `CPP`

### 입출력 정책
- 입력: `stdin`만 사용
- 출력: `stdout`만 사용
- 에러 출력: `stderr`만 사용
- 파일 입출력: 지원하지 않음
- 네트워크 접근: 차단

### 지원 판정값
- `AC`
- `WA`
- `CE`
- `RE`
- `TLE`
- `MLE`

### 제출 상태값
- `PENDING`
- `RUNNING`
- `DONE`
- `FAILED`

---

## 3. 최소 DB 사용 범위

### `problems`
사용 컬럼:

- `problem_id`
- `problem_type`
- `time_limit`
- `memory_limit`
- `status`

채점 시스템 규칙:

- `problem_type = CODING` 인 문제만 채점
- `status = PUBLISHED` 인 문제만 채점

### `test_cases`
사용 컬럼:

- `test_case_id`
- `problem_id`
- `input_data`
- `expected_output`
- `is_sample`
- `case_order`

채점 시스템 규칙:

- `is_sample = false` 인 숨은 테스트케이스만 사용
- `case_order` 순서대로 실행

### `submissions`
사용 컬럼:

- `submission_id`
- `problem_id`
- `user_id`
- `language`
- `source_code`
- `status`
- `verdict`
- `exec_time_ms`
- `memory_kb`
- `error_message`
- `submitted_at`
- `judged_at`

---

## 4. 패키지 구조

```text
com.ssafy.passit
├─ common
│  ├─ exception
│  ├─ response
│  └─ type
├─ problem
│  ├─ mapper
│  ├─ model
│  └─ service
├─ submission
│  ├─ controller
│  ├─ dto
│  ├─ mapper
│  ├─ model
│  └─ service
└─ judge
   ├─ dto
   ├─ runner
   ├─ service
   └─ support
```

채점기 MVP 단계에서는 `user`, `community` 전체 구현이 없어도 됩니다.

---

## 5. 핵심 Enum

### `LanguageType`
- `PYTHON`
- `JAVA`
- `CPP`

### `SubmissionStatus`
- `PENDING`
- `RUNNING`
- `DONE`
- `FAILED`

### `VerdictType`
- `AC`
- `WA`
- `CE`
- `RE`
- `TLE`
- `MLE`

### `ProblemType`
- `QUIZ`
- `CODING`

### `ProblemStatus`
- `DRAFT`
- `VALIDATING`
- `PUBLISHED`
- `NEEDS_REVIEW`
- `REJECTED`

---

## 6. 주요 API

### 6.1 제출 생성

`POST /api/problems/{problemId}/submissions`

요청:

```json
{
  "userId": 1,
  "language": "PYTHON",
  "sourceCode": "print(input())"
}
```

응답:

```json
{
  "submissionId": 10,
  "status": "DONE",
  "verdict": "AC",
  "execTimeMs": 15,
  "memoryKb": 12000,
  "errorMessage": null
}
```

### 6.2 제출 결과 조회

`GET /api/submissions/{submissionId}`

응답:

```json
{
  "submissionId": 10,
  "problemId": 3,
  "userId": 1,
  "language": "PYTHON",
  "status": "DONE",
  "verdict": "AC",
  "execTimeMs": 15,
  "memoryKb": 12000,
  "errorMessage": null,
  "submittedAt": "2026-05-22T12:00:00",
  "judgedAt": "2026-05-22T12:00:01"
}
```

---

## 7. 요청 흐름

### 7.1 제출 흐름

1. 클라이언트가 제출 요청을 보냅니다.
2. `SubmissionController`가 요청을 받습니다.
3. `SubmissionService`가 아래를 검증합니다.
   - 문제 존재 여부
   - 코딩 문제인지 여부
   - 공개된 문제인지 여부
   - 지원 언어인지 여부
   - 소스코드가 비어 있지 않은지 여부
4. `submissions`에 `PENDING` 상태로 저장합니다.
5. `JudgeService.judge(submissionId)`를 호출합니다.
6. 최종 제출 결과를 응답으로 반환합니다.

### 7.2 채점 흐름

1. 제출 정보를 조회합니다.
2. 제출 상태를 `RUNNING`으로 변경합니다.
3. 문제의 시간 제한과 메모리 제한을 조회합니다.
4. 숨은 테스트케이스를 조회합니다.
5. 언어에 맞는 Runner를 선택합니다.
6. 테스트케이스를 순서대로 실행합니다.
7. 첫 번째 비정상 판정이 나오면 즉시 종료합니다.
8. 아래 결과를 종합합니다.
   - 최종 판정
   - 최대 실행 시간
   - 최대 메모리 사용량
   - 에러 메시지
9. 제출 상태를 `DONE`으로 저장합니다.
10. 인프라 오류가 나면 제출 상태를 `FAILED`로 저장합니다.

---

## 8. 클래스 설계

### 8.1 `SubmissionController`

역할:

- 제출 요청 받기
- 제출 결과 응답 반환

메서드:

```java
SubmissionResultResponse createSubmission(Long problemId, CreateSubmissionRequest request)
SubmissionResultResponse getSubmission(Long submissionId)
```

### 8.2 `SubmissionService`

역할:

- 제출 요청 검증
- 최초 제출 데이터 저장
- 채점 서비스 호출
- 최종 결과 조회

메서드:

```java
SubmissionResultResponse submit(Long problemId, CreateSubmissionRequest request)
Submission findById(Long submissionId)
void markRunning(Long submissionId)
void markDone(JudgeResult result)
void markFailed(Long submissionId, String errorMessage)
```

### 8.3 `JudgeService`

역할:

- 채점 전체 오케스트레이션
- 문제/테스트케이스 조회
- 언어별 Runner 선택
- 테스트케이스 반복 실행
- 최종 채점 결과 생성

메서드:

```java
JudgeResult judge(Long submissionId)
```

### 8.4 `CodeRunner`

역할:

- Docker 안에서 테스트케이스 1건 실행
- 원시 실행 결과 반환

```java
public interface CodeRunner {
    LanguageType supportLanguage();
    ExecutionResult run(ExecutionRequest request);
}
```

구현체:

- `PythonCodeRunner`
- `JavaCodeRunner`
- `CppCodeRunner`

### 8.5 `DockerCommandFactory`

역할:

- OS에 맞는 Docker 명령 생성
- 메모리/CPU/네트워크 제한 옵션 중앙 관리

메서드:

```java
List<String> buildPythonCommand(ExecutionRequest request)
List<String> buildJavaCommand(ExecutionRequest request)
List<String> buildCppCommand(ExecutionRequest request)
```

### 8.6 `WorkspaceManager`

역할:

- 임시 작업 디렉터리 생성
- 소스 파일 저장
- 실행 후 정리

메서드:

```java
Path createWorkspace(Long submissionId, LanguageType language)
Path writeSourceFile(Path workspace, String sourceCode, LanguageType language)
void deleteWorkspace(Path workspace)
```

### 8.7 `ProcessExecutor`

역할:

- `ProcessBuilder`로 Docker 프로세스 실행
- stdin 쓰기
- stdout/stderr 읽기
- timeout 제어

메서드:

```java
ProcessExecutionResult execute(List<String> command, String stdin, long timeoutMs)
```

### 8.8 `OutputComparator`

역할:

- 정답 출력과 실제 출력 비교
- 줄바꿈 정규화
- 필요 시 trailing whitespace 무시

메서드:

```java
boolean isMatch(String expected, String actual)
```

---

## 9. DTO 설계

### 9.1 Submission DTO

```java
public record CreateSubmissionRequest(
    Long userId,
    String language,
    String sourceCode
) {}
```

```java
public record SubmissionResultResponse(
    Long submissionId,
    Long problemId,
    Long userId,
    String language,
    String status,
    String verdict,
    Integer execTimeMs,
    Integer memoryKb,
    String errorMessage,
    LocalDateTime submittedAt,
    LocalDateTime judgedAt
) {}
```

### 9.2 Judge DTO

```java
public record ExecutionRequest(
    Long submissionId,
    Long problemId,
    LanguageType language,
    String sourceCode,
    String stdin,
    Integer timeLimitMs,
    Integer memoryLimitMb
) {}
```

```java
public record ExecutionResult(
    VerdictType verdict,
    Integer execTimeMs,
    Integer memoryKb,
    String stdout,
    String stderr,
    String errorMessage
) {}
```

```java
public record JudgeResult(
    Long submissionId,
    SubmissionStatus status,
    VerdictType verdict,
    Integer execTimeMs,
    Integer memoryKb,
    String errorMessage
) {}
```

---

## 10. Mapper 설계

### `ProblemMapper`

필수 메서드:

```java
Problem findPublishedCodingProblemById(Long problemId)
```

SQL 조건:

- `problem_type = 'CODING'`
- `status = 'PUBLISHED'`

### `TestCaseMapper`

필수 메서드:

```java
List<TestCase> findHiddenTestCasesByProblemId(Long problemId)
```

SQL 조건:

- `is_sample = false`
- `case_order asc`

### `SubmissionMapper`

필수 메서드:

```java
int insertSubmission(Submission submission)
Submission findById(Long submissionId)
int updateStatusRunning(Long submissionId)
int updateJudgeSuccess(Submission submission)
int updateJudgeFailure(Long submissionId, String errorMessage)
```

메모:

- `insertSubmission`은 `useGeneratedKeys` 사용
- 상태 변경 쿼리는 명확하게 분리

---

## 11. 모델 설계

### `Problem`

필드:

- `problemId`
- `problemType`
- `timeLimit`
- `memoryLimit`
- `status`

### `TestCase`

필드:

- `testCaseId`
- `problemId`
- `inputData`
- `expectedOutput`
- `isSample`
- `caseOrder`

### `Submission`

필드:

- `submissionId`
- `problemId`
- `userId`
- `language`
- `sourceCode`
- `status`
- `verdict`
- `execTimeMs`
- `memoryKb`
- `errorMessage`
- `submittedAt`
- `judgedAt`

---

## 12. Runner 전략

각 Runner는 테스트케이스 1건씩 실행합니다.

### 12.1 Python Runner

소스 파일명:

- `main.py`

Docker 이미지:

- `python:3.11-alpine`

Docker 명령 형태:

```text
docker run --rm
  --network none
  --memory {memoryLimitMb}m
  --cpus 1
  --volume {workspace}:/workspace
  --workdir /workspace
  python:3.11-alpine
  python3 main.py
```

### 12.2 Java Runner

소스 파일명:

- `Main.java`

Docker 이미지:

- `eclipse-temurin:21`

Docker 명령 형태:

```text
docker run --rm
  --network none
  --memory {memoryLimitMb}m
  --cpus 1
  --volume {workspace}:/workspace
  --workdir /workspace
  eclipse-temurin:21
  sh -c "javac Main.java && java Main"
```

### 12.3 C++ Runner

소스 파일명:

- `main.cpp`

Docker 이미지:

- `gcc:13`

Docker 명령 형태:

```text
docker run --rm
  --network none
  --memory {memoryLimitMb}m
  --cpus 1
  --volume {workspace}:/workspace
  --workdir /workspace
  gcc:13
  sh -c "g++ -O2 -std=c++17 main.cpp -o main && ./main"
```

---

## 13. Docker 실행 방식 설명

Python 기준 예시:

1. `C:\...\temp\submission-15` 같은 임시 디렉터리를 생성합니다.
2. 그 디렉터리에 `main.py`를 저장합니다.
3. Docker를 실행하면서 해당 디렉터리를 `/workspace`로 마운트합니다.
4. 컨테이너 안에서는 `/workspace/main.py`가 보입니다.
5. 컨테이너 안에서 `python3 main.py`를 실행합니다.
6. 자바 프로세스가 테스트 입력을 stdin으로 넣습니다.
7. 자바 프로세스가 stdout/stderr를 읽습니다.
8. 컨테이너는 종료 후 자동 삭제됩니다.

즉:

- 호스트 PC에 Python이나 g++가 직접 설치되어 있을 필요는 없음
- 컴파일러/인터프리터는 Docker 이미지 안에 들어 있음

---

## 14. 시간/자원 제한 정책

### Timeout

문제의 `time_limit`를 기준으로 사용합니다.

MVP 권장 규칙:

- 실제 컨테이너 timeout = `time_limit + 1000ms`

이유:

- 컨테이너 기동 오버헤드 완충

timeout 초과 시:

- 프로세스 강제 종료
- `TLE` 반환

### 메모리 제한

문제의 `memory_limit`를 Docker `--memory` 값으로 사용합니다.

메모리 초과가 감지되면:

- 가능하면 `MLE`
- MVP에서는 구분이 애매하면 `RE`로 처리 가능

### CPU

- 고정값 `--cpus 1`

### 네트워크

- `--network none`

---

## 15. 판정 정책

### `AC`
- 정상 종료
- stdout이 정답과 일치

### `WA`
- 정상 종료
- stdout이 정답과 불일치

### `CE`
- 컴파일 실패
- 주로 Java, C++

### `RE`
- 런타임 비정상 종료
- 예외 발생
- 비정상 프로세스 종료

### `TLE`
- timeout 초과

### `MLE`
- 메모리 제한 초과가 확인된 경우

### `FAILED`
사용자 코드 문제가 아니라 인프라 수준 실패:

- Docker 명령 실행 실패
- 임시 디렉터리 생성 실패
- 채점 중 예기치 않은 시스템 예외 발생

이 경우:

- `submission.status = FAILED`
- `submission.verdict = null`

---

## 16. 출력 비교 정책

MVP 권장 정규화 규칙:

1. `\r\n`을 `\n`으로 통일
2. 각 줄의 뒤쪽 공백 제거
3. 마지막 개행 정리
4. 최종 문자열 정확 비교

이 방식은 Windows/Linux 줄바꿈 차이 때문에 틀리는 문제를 줄여줍니다.

---

## 17. 예외 처리 정책

### 사용자 코드 오류

- 컴파일 실패 -> `CE`
- 런타임 예외 -> `RE`
- 정답 불일치 -> `WA`

이 경우도 채점은 정상 종료된 것이므로:

- `submission.status = DONE`

### 시스템 오류

- Docker 데몬 사용 불가
- workspace 파일 처리 실패
- 채점 서비스 내부 예외

이 경우:

- `submission.status = FAILED`
- `error_message`에 시스템 오류 요약 저장

---

## 18. 동기식 MVP 결정

1차 버전은 **동기식 채점**으로 갑니다.

이유:

- 구현이 가장 단순함
- 디버깅이 쉬움
- 큐가 필요 없음

단점:

- 요청 하나가 몇 초 걸릴 수 있음

나중 확장 방식:

- `PENDING` 저장 후 submission ID만 먼저 반환
- 백그라운드 워커가 비동기 채점

이 확장은 현재 스키마를 바꾸지 않고도 가능합니다.

---

## 19. 구현 순서 추천

### Step 1
- enum 추가
- 공통 예외 처리 추가

### Step 2
- `Problem`, `TestCase`, `Submission` 모델 추가
- MyBatis Mapper와 XML 추가

### Step 3
- `SubmissionController` 추가
- 제출 생성/조회 API 추가

### Step 4
- `JudgeService` 구현
- `WorkspaceManager` 구현
- `ProcessExecutor` 구현

### Step 5
- `PythonCodeRunner` 구현
- Python 기준 end-to-end 검증

### Step 6
- `JavaCodeRunner` 추가
- `CppCodeRunner` 추가

### Step 7
- 에러 매핑 정교화
- 출력 비교 로직 보강

---

## 20. 1차 완료 기준

아래가 모두 되면 1차 채점기 MVP 완료로 봅니다.

1. DB에 공개된 코딩 문제가 존재함
2. 해당 문제에 숨은 테스트케이스가 존재함
3. 클라이언트가 Python 코드를 제출함
4. 시스템이 `submissions` row를 생성함
5. Docker 안에서 코드가 실행됨
6. 최종 판정이 DB에 저장됨
7. `GET /api/submissions/{id}`로 결과 조회가 됨

이게 안정화되면 Java와 C++를 붙입니다.
