# Pass-IT 🎯

> IT 자격증 문제 풀이 플랫폼

---

## 📌 프로젝트 개요

**Pass-IT**은 IT 자격증 취득을 준비하는 사람들을 위한 문제 풀이 플랫폼입니다.
AI 기반 문제 생성, 사용자 직접 출제, 커뮤니티 기능, 웹 기반 코드 실행 환경을 통해
자격증 학습의 전 과정을 지원합니다.

---

## 💡 주요 기능

- **AI 문제 생성** — 기출 문제 PDF를 학습한 AI가 새로운 문제를 자동 생성하고 품질을 검수
- **직접 출제** — 사용자가 직접 문제를 만들어 공유하거나, 다른 사람이 만든 문제를 풀 수 있음
- **커뮤니티** — 자격증별 후기, 팁, 정보 공유 게시판
- **웹 IDE** — 프로그래밍 언어 관련 문제를 브라우저에서 바로 풀 수 있는 코드 실행 환경

---

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| 프론트엔드 | 미정 |
| 백엔드 | Java, Spring Boot, Spring Data JPA |
| 데이터베이스 | MySQL |
| CI/CD | Jenkins |
| 배포 | Docker, AWS |
| 인공지능 | RAG (검색 증강 생성), LangChain |
| 협업 | Jira, Confluence, Figma |

---

## 📋 WBS (작업 분류 체계)

![WBS](.images/wbs.svg)

---

## 📊 간트 차트 (16주 일정)

![간트 차트](./images/gantt.svg)

---

## 🔷 유스케이스 다이어그램

![유스케이스 다이어그램](./images/usecase-diagram.svg)

### 액터 설명

| 액터 | 설명 |
|------|------|
| 비회원 | 회원가입 없이 문제 목록 조회 및 커뮤니티 열람 가능 |
| 회원 | 문제 풀기, 출제, AI 생성 요청, 커뮤니티 활동 가능 |
| 관리자 | 문제 승인/거절, 회원 관리 등 플랫폼 운영 담당 |
| AI 시스템 | 기출 데이터 학습 기반으로 문제 자동 생성 및 검수 수행 |

---

## 🗓 마일스톤

| 주차 | 마일스톤 | 내용 |
|------|----------|------|
| W4 | 인프라 완료 | AWS, Docker, Jenkins, DB 설계 완료 |
| W8 | AI 1차 완료 | RAG 파이프라인 및 데이터 전처리 완료 |
| W12 | 개발 완료 | 백엔드·프론트엔드·AI 주요 기능 개발 완료 |
| W16 | 최종 배포 | 베타 테스트 반영 후 운영 환경 배포 |

---

## 📁 협업 도구

- **Jira** — 스프린트 및 이슈 관리
- **Confluence** — 문서화 및 회의록
- **Figma** — UI/UX 디자인 협업

---

## 화면 설계

### 메인 화면
![메인화면](./images/c1.png)
### 문제 조회 화면
![문제풀기](./images/c2.png)
### 문제 생성 화면
![문제생성](./images/c3.png)
### 커뮤니티 화면
![커뮤니티](./images/c4.png)    

---

## DB 스키마 변경 보고서 (2026-06-10)

### 변경 요약

| 항목 | 기존 | 변경 후 |
|------|------|---------|
| 문제 제공 단위 | `problem_sets` 기반 문제 세트 | 개별 문제 단위 |
| 공통 문제 테이블 | `problems`에 문제 본문/정답 포함 | `problems`는 공통 메타 정보만 보관 |
| 객관식 구조 | `problems` + `problem_choices` | `multiple_choice_problems`로 통합 |
| 주관식 구조 | `problems.answer_text` 사용 | `short_answer_problems` 분리 |
| 코딩 문제 구조 | `coding_problems` 독립 운영 | `problems` 하위 상세 테이블로 편입 |

### 테이블 변경 내역

| 구분 | 테이블 | 변경 내용 |
|------|--------|-----------|
| 삭제 | `problem_sets` | 문제 세트 개념 제거 |
| 삭제 | `problem_choices` | 객관식 보기를 별도 테이블로 관리하던 구조 제거 |
| 변경 | `problems` | `problem_set_id`, `problem_number`, `question`, `answer_correct_number`, `answer_text` 제거, `cert_id`, `problem_type` 중심으로 재구성 |
| 신설 | `multiple_choice_problems` | 객관식 문제 본문, 4개 선택지, 정답 번호 저장 |
| 신설 | `short_answer_problems` | 주관식 문제 본문과 정답 저장 |
| 변경 | `coding_problems` | 독립 문제 테이블에서 `problems.problem_id`를 참조하는 상세 테이블로 변경 |

### 관계 구조 비교

| 구분 | 기존 구조 | 변경 후 구조 |
|------|-----------|-------------|
| 일반 문제 흐름 | `certifications -> problem_sets -> problems -> problem_choices` | `certifications -> problems -> multiple_choice_problems / short_answer_problems / coding_problems` |
| 코딩 문제 흐름 | `coding_problems -> test_cases, submissions` | `problems -> coding_problems -> test_cases, submissions` |
| 자격증 연결 기준 | `problem_sets.cert_id` | `problems.cert_id` |

### 설계상 의미

| 항목 | 변경 의미 |
|------|-----------|
| 문제 관리 방식 | 세트 중심 관리에서 문제 중심 관리로 변경 |
| 타입 분리 | 객관식, 주관식, 코딩 문제를 타입별 상세 테이블로 명확히 분리 |
| 확장성 | 새로운 문제 유형 추가 시 `problems` 하위 상세 테이블 추가 방식으로 확장 가능 |
| 조회 구조 | 문제 공통 정보와 상세 정보를 역할별로 분리해 구조 명확성 향상 |
