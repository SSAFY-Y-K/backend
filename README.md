# Pass-IT

자격증 / 코딩 학습을 뮈한 문제 풀이 플랫폼

---

## 0. 목차

---
1. [프로젝트 소개](#1-프로젝트-소개)
2. [팀원 소개](#2-팀원-소개)
3. [개발 일정](#3-개발-일정)
4. [기술 스택](#4-기술-스택)
5. [프로젝트 구조](#5-프로젝트-구조)
6. [실행 가이드](#6-실행-가이드)
7. [주요 기능 소개](#7-주요-기능-소개)
8. [상세 담당 업무](#8-상세-담당-업무)

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

| 영역         | 기술                                                  |
|------------|-----------------------------------------------------|
| Backend    | Java 21, Spring Boot 4, Spring Security, MyBatis    |
| Frontend   | Vue 3, Vite, Pinia, Vue Router, Axios, Tailwind CSS |
| Database   | MySQL 8                                             |
| Judge      | Docker, Python 3.11, Eclipse Temurin 21, GCC 13     |
| Deployment | Docker, Docker Compose, Nginx, AWS EC2, RDS, ECR    |
| AI 연동      | 별도 AI 서버, OpenAI 호환 API, Chroma 기반 RAG              |

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

## 7. 주요 기능 소개

--- 

## 8. 상세 담당 업무