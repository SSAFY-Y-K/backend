# Pass-IT

자격증 / 코딩 학습을 뮈한 문제 풀이 웹 서비스

---

## 0. 목차

---
1. [프로젝트 소개](#1-프로젝트-소개)
2. [팀원 소개](#2-팀원-소개)
3. [개발 일정](#3-개발-일정)
4. [기술 스택](#4-기술-스택)
5. [프로젝트 구조](#5-프로젝트-구조)
6. [실행 가이드](#6-실행-가이드)
7. [주요 기능 소개](#7-주요-기능-소개-및-담당자)
8. [트러블 슈팅](#8-트러블-슈팅)
9. [회고](#9-회고)

## 1. 프로젝트 소개

---

Pass-IT은 자격증 및 알고리즘을 문제 풀이를 제공하는 웹 서비스입니다.

문제를 푸는 것 뿐만이 아닌 직접 문제를 생성하거나 AI를 통한 문제 생성이 가능합니다.

또한 사용자 간 커뮤니티 기능을 제공하여 다양항 사용자들과 합격 후기/팁 등을 공유할 수 있습니다.

## 2. 팀원 소개

---

| 윤영웅                            | 김효찬                           |
|--------------------------------|-------------------------------|


## 3. 개발 일정

---

프로젝트 개발 기간: 2026.05 ~ 2026.06 (약 2달)

## 4. 기술 스택

---

|       영역       |                        기술                        |
|:--------------:|:------------------------------------------------:|
|    Backend     | Java 21, Spring Boot 4, Spring Security, MyBatis |
|    Frontend    |         Vue 3, Vite, Pinia, Tailwind CSS         |
|    Database    |                    MySQL 8.0                     |
|     Judge      | Docker, Python 3.11, Eclipse Temurin 21, GCC 13  |
| Infrastructure | Docker, Docker Compose, Nginx, AWS EC2, RDS, ECR |
|     AI 연동      |      별도 AI 서버, OpenAI 호환 API, Chroma 기반 RAG      |

## 5. 프로젝트 구조

--- 

```text
.
|-- src/main/java/com/ssafy/passit
|   |-- auth              # 로그인, 로그아웃, 토큰 재발급
|   |-- security          # JWT 필터, 인증 진입점, Security 설정
|   |-- user              # 회원 가입, 내 정보, 마이페이지
|   |-- certification     # 자격증 목록, 자격증별 문제 수
|   |-- problem           # 일반 문제, 코딩 문제, AI 문제 생성
|   |-- judge             # 코드 실행/채점 오케스트레이션
|   |-- submission        # 제출 생성 및 결과 조회
|   |-- post              # 게시글, 댓글
|   |-- report            # 문제 신고 및 관리자 처리
|   |-- common            # 공통 응답, 예외, enum
|   `-- admin             # 초기 관리자 계정 생성
|-- src/main/resources
|   |-- db/migration      # Flyway DB 마이그레이션
|   |-- mapper            # MyBatis XML mapper
|   `-- application.properties
|-- frontend              # Vue 프론트엔드
|-- docs                  # 설계/배포 보조 문서
|-- images                # 화면/설계 이미지
|-- docker-compose.yml    # 로컬 MySQL 실행용
|-- docker-compose.aws.yml
|-- Dockerfile            # 백엔드 이미지
`-- scripts/deploy-stack.sh
```

### 프론트엔드 구조

```text
frontend
|-- public                # 정적 아이콘, favicon
|-- src
|   |-- api               # Axios 인스턴스, API 호출 함수
|   |-- components        # 공통 컴포넌트, 자격증 문제 생성 폼
|   |-- router            # Vue Router 경로 설정
|   |-- stores            # Pinia 인증/자격증 상태 관리
|   |-- utils             # 텍스트 정규화 등 공통 유틸
|   |-- views             # 화면 단위 Vue 컴포넌트
|   |-- App.vue
|   |-- main.js
|   `-- style.css
|-- Dockerfile            # 프론트엔드 빌드 및 Nginx 이미지
|-- nginx.conf            # 운영 배포 시 /api 프록시 설정
|-- package.json          # npm 스크립트 및 의존성
`-- vite.config.js        # Vite 설정, 개발 서버 /api 프록시
```
### 데이터베이스 구조
![ERD](/images/erd.png)


### 도메인별 구조

#### 회원

- `users`
- 서비스 사용자의 계정 정보를 관리한다.
- `username`과 `nickname`은 각각 unique 제약조건을 가진다.
- `role`은 `USER`, `ADMIN`만 허용된다.

#### 자격증

- `certifications`
- 문제와 게시글이 참조하는 자격증 기준 데이터이다.
- 자격증 이름은 중복될 수 없다.

#### 자격증 문제

- `problems`
- `multiple_choice_problems`
- `short_answer_problems`

`problems`는 문제의 공통 정보를 저장하고, 문제 유형에 따라 객관식 또는 주관식 상세 테이블과 1:1로 연결된다.

#### 코딩 문제 및 채점

- `coding_problems`
- `test_cases`
- `submissions`

`coding_problems`는 알고리즘 문제 자체를 저장한다.  
`test_cases`는 해당 문제의 예제 및 숨은 테스트케이스를 저장한다.  
`submissions`는 사용자의 코드 제출과 채점 결과를 저장한다.

#### 커뮤니티

- `posts`
- `comments`

사용자는 자격증과 관련된 게시글을 작성할 수 있고, 게시글에는 댓글을 작성할 수 있다.

#### 문제 신고

- `problem_reports`

사용자가 코딩 문제에 대해 오류를 신고할 수 있다.  
신고 상태는 `PENDING`, `RESOLVED`로 관리된다.

## 6. 실행 가이드

--- 

### 필요 도구

- JDK 21
- Docker Desktop
- Node.js `20.19.0` 이상 또는 `22.12.0` 이상
- npm

### 1. MySQL 실행

```powershell
docker compose up -d mysql
```

### 2. 백엔드 실행

Windows:

```powershell
.\gradlew.bat bootRun
```

macOS/Linux:

```bash
./gradlew bootRun
```

### 3. 프론트엔드 실행

```powershell
cd frontend
npm ci
npm run dev
```

## 7. 주요 기능 소개 및 담당자

---

### AI 서버 - 윤영웅, 김효찬
- 사용자의 요청에 맞게 문제를 생성하는 Python 기반 AI 서버
- 객관식/주관식/코딩 문제를 생성

### 회원/인증 - 윤영웅
- 회원가입/로그인/로그아웃
- JWT를 활용한 사용자 인증/인가
- 닉네임 변경

### 자격증 문제 생성/조회 - 윤영웅
- 서버에 등록된 자격증 목록 조회
- 직접 혹은 AI 기반 자격증 문제 생성
- 생성된 문제 조회 및 풀이

### 코딩 문제 생성/풀이/채점 - 김효찬
- AI 기반 코딩 문제 생성
- 생성된 코딩 문제 실행 및 채점
- 제출 이력 조회

### 게시글 및 댓글 - 김효찬
- 게시글 작성 및 조회, 수정, 삭제
- 댓글 작성 및 조회, 수정, 삭제
- 자신이 작성한 글 조회

### 문제 오류 신고/관리자 전용 - 김효찬
- 오류가 있는 코딩 문제를 사용자가 신고
- 관리자는 신고 내역을 조회하고 처리 가능

## 8. 트러블 슈팅

---

## 9. 회고

--- 