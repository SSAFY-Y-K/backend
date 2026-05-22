-- =============================================================
-- Pass-IT Database Initialization Script
-- =============================================================

USE passit;

-- -------------------------------------------------------------
-- 1. USERS (회원)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    user_id    BIGINT       NOT NULL AUTO_INCREMENT,
    login_id   VARCHAR(50)  NOT NULL,
    password   VARCHAR(255) NOT NULL,
    nickname   VARCHAR(50)  NOT NULL,
    role       VARCHAR(20)  NOT NULL DEFAULT 'USER',
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id),
    UNIQUE KEY uq_users_login_id (login_id),
    UNIQUE KEY uq_users_nickname (nickname),
    CONSTRAINT chk_users_role CHECK (role IN ('USER', 'ADMIN'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- 2. CERTIFICATIONS (자격증)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS certifications (
    cert_id BIGINT       NOT NULL AUTO_INCREMENT,
    name    VARCHAR(100) NOT NULL,

    PRIMARY KEY (cert_id),
    UNIQUE KEY uq_certifications_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 초기 자격증 데이터
INSERT IGNORE INTO certifications (name) VALUES
    ('정보처리기사'),
    ('SQLD');

-- -------------------------------------------------------------
-- 3. PROBLEMS (문제 메타)
--    상태는 관리자 승인 대신 자동 검수 파이프라인 기준으로 관리
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS problems (
    problem_id             BIGINT        NOT NULL AUTO_INCREMENT,
    cert_id                BIGINT        NOT NULL,
    author_id              BIGINT        NULL,
    problem_type           VARCHAR(20)   NOT NULL DEFAULT 'QUIZ' COMMENT 'QUIZ / CODING',
    title                  VARCHAR(255)  NOT NULL,
    description            TEXT          NOT NULL COMMENT '문제 본문',
    input_description      TEXT          NULL COMMENT '코딩 문제 입력 설명',
    output_description     TEXT          NULL COMMENT '코딩 문제 출력 설명',
    constraint_text        TEXT          NULL COMMENT '제한 사항',
    time_limit             INT           NOT NULL DEFAULT 5000 COMMENT '제한 시간 (ms)',
    memory_limit           INT           NOT NULL DEFAULT 256 COMMENT '메모리 제한 (MB)',
    source_type            VARCHAR(10)   NOT NULL COMMENT 'AI / USER / EXAM',
    status                 VARCHAR(20)   NOT NULL DEFAULT 'DRAFT' COMMENT 'DRAFT / VALIDATING / PUBLISHED / NEEDS_REVIEW / REJECTED',
    last_validation_score  DECIMAL(5,2)  NULL COMMENT '가장 최근 자동 검수 점수',
    published_at           DATETIME      NULL,
    created_at             DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at             DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (problem_id),
    KEY idx_problems_cert_status (cert_id, status),
    KEY idx_problems_source_status (source_type, status),
    CONSTRAINT fk_problems_cert FOREIGN KEY (cert_id) REFERENCES certifications (cert_id),
    CONSTRAINT fk_problems_author FOREIGN KEY (author_id) REFERENCES users (user_id),
    CONSTRAINT chk_problems_problem_type CHECK (problem_type IN ('QUIZ', 'CODING')),
    CONSTRAINT chk_problems_source_type CHECK (source_type IN ('AI', 'USER', 'EXAM')),
    CONSTRAINT chk_problems_status CHECK (status IN ('DRAFT', 'VALIDATING', 'PUBLISHED', 'NEEDS_REVIEW', 'REJECTED')),
    CONSTRAINT chk_problems_validation_score CHECK (last_validation_score IS NULL OR (last_validation_score >= 0 AND last_validation_score <= 100)),
    CONSTRAINT chk_problems_time_limit CHECK (time_limit >= 1),
    CONSTRAINT chk_problems_memory_limit CHECK (memory_limit >= 16)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- 4. PROBLEM_OPTIONS (객관식 선택지)
--    QUIZ 문제에서 선택지 렌더링에 사용
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS problem_options (
    option_id      BIGINT   NOT NULL AUTO_INCREMENT,
    problem_id     BIGINT   NOT NULL,
    option_order   INT      NOT NULL,
    option_text    TEXT     NOT NULL,
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (option_id),
    UNIQUE KEY uq_problem_options_problem_order (problem_id, option_order),
    KEY idx_problem_options_problem (problem_id),
    CONSTRAINT fk_problem_options_problem FOREIGN KEY (problem_id) REFERENCES problems (problem_id) ON DELETE CASCADE,
    CONSTRAINT chk_problem_options_order CHECK (option_order >= 1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- 5. PROBLEM_SOLUTIONS (정답 / 해설 / 참고 코드)
--    정답 텍스트, 해설, 코딩 문제 reference solution을 모두 저장
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS problem_solutions (
    solution_id      BIGINT       NOT NULL AUTO_INCREMENT,
    problem_id       BIGINT       NOT NULL,
    solution_type    VARCHAR(20)  NOT NULL COMMENT 'ANSWER / EXPLANATION / REFERENCE_CODE',
    content_format   VARCHAR(20)  NOT NULL DEFAULT 'TEXT' COMMENT 'TEXT / CODE / OPTION_ORDER / JSON',
    language         VARCHAR(20)  NULL COMMENT 'PYTHON / JAVA / CPP',
    content          LONGTEXT     NOT NULL,
    is_public        BOOLEAN      NOT NULL DEFAULT FALSE COMMENT '사용자 공개 여부',
    solution_order   INT          NOT NULL DEFAULT 1,
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (solution_id),
    KEY idx_problem_solutions_problem_type (problem_id, solution_type, solution_order),
    KEY idx_problem_solutions_problem_public (problem_id, is_public),
    CONSTRAINT fk_problem_solutions_problem FOREIGN KEY (problem_id) REFERENCES problems (problem_id) ON DELETE CASCADE,
    CONSTRAINT chk_problem_solutions_type CHECK (solution_type IN ('ANSWER', 'EXPLANATION', 'REFERENCE_CODE')),
    CONSTRAINT chk_problem_solutions_format CHECK (content_format IN ('TEXT', 'CODE', 'OPTION_ORDER', 'JSON')),
    CONSTRAINT chk_problem_solutions_language CHECK (language IS NULL OR language IN ('PYTHON', 'JAVA', 'CPP')),
    CONSTRAINT chk_problem_solutions_order CHECK (solution_order >= 1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- 6. TEST_CASES (코딩 문제 테스트케이스)
--    공개 예제와 숨은 채점 케이스를 함께 저장
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS test_cases (
    test_case_id      BIGINT      NOT NULL AUTO_INCREMENT,
    problem_id        BIGINT      NOT NULL,
    input_data        MEDIUMTEXT  NOT NULL COMMENT 'stdin 입력',
    expected_output   MEDIUMTEXT  NOT NULL COMMENT 'stdout 정답',
    is_sample         BOOLEAN     NOT NULL DEFAULT FALSE COMMENT '예제 공개 여부',
    case_order        INT         NOT NULL DEFAULT 1,
    created_at        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (test_case_id),
    UNIQUE KEY uq_test_cases_problem_sample_order (problem_id, is_sample, case_order),
    KEY idx_test_cases_problem_sample_order (problem_id, is_sample, case_order),
    CONSTRAINT fk_test_cases_problem FOREIGN KEY (problem_id) REFERENCES problems (problem_id) ON DELETE CASCADE,
    CONSTRAINT chk_test_cases_case_order CHECK (case_order >= 1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- 7. PROBLEM_VALIDATION_RUNS (자동 검수 실행 이력)
--    문제 생성/수정 시마다 자동 검수 파이프라인의 상위 실행 단위
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS problem_validation_runs (
    validation_run_id  BIGINT        NOT NULL AUTO_INCREMENT,
    problem_id         BIGINT        NOT NULL,
    trigger_type       VARCHAR(20)   NOT NULL COMMENT 'CREATE / UPDATE / REPUBLISH / MANUAL',
    overall_status     VARCHAR(20)   NOT NULL DEFAULT 'RUNNING' COMMENT 'RUNNING / PASSED / FAILED / NEEDS_REVIEW',
    quality_score      DECIMAL(5,2)  NULL,
    summary_message    VARCHAR(255)  NULL,
    started_at         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finished_at        DATETIME      NULL,
    created_at         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (validation_run_id),
    KEY idx_validation_runs_problem_started (problem_id, started_at),
    KEY idx_validation_runs_status (overall_status),
    CONSTRAINT fk_validation_runs_problem FOREIGN KEY (problem_id) REFERENCES problems (problem_id) ON DELETE CASCADE,
    CONSTRAINT chk_validation_runs_trigger CHECK (trigger_type IN ('CREATE', 'UPDATE', 'REPUBLISH', 'MANUAL')),
    CONSTRAINT chk_validation_runs_status CHECK (overall_status IN ('RUNNING', 'PASSED', 'FAILED', 'NEEDS_REVIEW')),
    CONSTRAINT chk_validation_runs_quality_score CHECK (quality_score IS NULL OR (quality_score >= 0 AND quality_score <= 100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- 8. PROBLEM_VALIDATION_LOGS (자동 검수 상세 로그)
--    구조 검사, 정답 검증, 테스트케이스 품질 검증 등의 결과 저장
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS problem_validation_logs (
    validation_log_id  BIGINT        NOT NULL AUTO_INCREMENT,
    validation_run_id  BIGINT        NOT NULL,
    stage_type         VARCHAR(20)   NOT NULL COMMENT 'STRUCTURE / SOLUTION / TEST_CASE / DUPLICATE / QUALITY',
    validator_type     VARCHAR(20)   NOT NULL COMMENT 'RULE / RUNNER / LLM',
    rule_code          VARCHAR(50)   NOT NULL,
    result_status      VARCHAR(20)   NOT NULL COMMENT 'PASSED / FAILED / WARNING',
    score              DECIMAL(5,2)  NULL,
    message            VARCHAR(255)  NULL,
    details_json       JSON          NULL,
    created_at         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (validation_log_id),
    KEY idx_validation_logs_run_stage (validation_run_id, stage_type),
    KEY idx_validation_logs_rule_code (rule_code),
    CONSTRAINT fk_validation_logs_run FOREIGN KEY (validation_run_id) REFERENCES problem_validation_runs (validation_run_id) ON DELETE CASCADE,
    CONSTRAINT chk_validation_logs_stage CHECK (stage_type IN ('STRUCTURE', 'SOLUTION', 'TEST_CASE', 'DUPLICATE', 'QUALITY')),
    CONSTRAINT chk_validation_logs_validator CHECK (validator_type IN ('RULE', 'RUNNER', 'LLM')),
    CONSTRAINT chk_validation_logs_result CHECK (result_status IN ('PASSED', 'FAILED', 'WARNING')),
    CONSTRAINT chk_validation_logs_score CHECK (score IS NULL OR (score >= 0 AND score <= 100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- 9. SUBMISSIONS (사용자 코드 제출 기록)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS submissions (
    submission_id   BIGINT        NOT NULL AUTO_INCREMENT,
    problem_id      BIGINT        NOT NULL,
    user_id         BIGINT        NOT NULL,
    language        VARCHAR(20)   NOT NULL COMMENT 'PYTHON / JAVA / CPP',
    source_code     LONGTEXT      NOT NULL,
    status          VARCHAR(20)   NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING / RUNNING / DONE / FAILED',
    verdict         VARCHAR(20)   NULL COMMENT 'AC / WA / CE / RE / TLE / MLE',
    exec_time_ms    INT           NULL COMMENT '실행 시간 (ms)',
    memory_kb       INT           NULL COMMENT '메모리 사용량 (KB)',
    error_message   TEXT          NULL COMMENT '컴파일 에러 또는 런타임 에러 메시지',
    submitted_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    judged_at       DATETIME      NULL,

    PRIMARY KEY (submission_id),
    KEY idx_submissions_problem_user_time (problem_id, user_id, submitted_at),
    KEY idx_submissions_user_time (user_id, submitted_at),
    KEY idx_submissions_status (status),
    CONSTRAINT fk_submissions_problem FOREIGN KEY (problem_id) REFERENCES problems (problem_id) ON DELETE CASCADE,
    CONSTRAINT fk_submissions_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT chk_submissions_language CHECK (language IN ('PYTHON', 'JAVA', 'CPP')),
    CONSTRAINT chk_submissions_status CHECK (status IN ('PENDING', 'RUNNING', 'DONE', 'FAILED')),
    CONSTRAINT chk_submissions_verdict CHECK (verdict IS NULL OR verdict IN ('AC', 'WA', 'CE', 'RE', 'TLE', 'MLE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- 10. POSTS (게시글)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS posts (
    post_id      BIGINT       NOT NULL AUTO_INCREMENT,
    user_id      BIGINT       NOT NULL,
    cert_id      BIGINT       NOT NULL,
    category     VARCHAR(20)  NOT NULL COMMENT 'REVIEW / TIP / QNA',
    title        VARCHAR(255) NOT NULL,
    content      TEXT         NOT NULL,
    view_count   INT          NOT NULL DEFAULT 0,
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (post_id),
    CONSTRAINT fk_posts_user FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_posts_cert FOREIGN KEY (cert_id) REFERENCES certifications (cert_id),
    CONSTRAINT chk_posts_category CHECK (category IN ('REVIEW', 'TIP', 'QNA'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- 11. COMMENTS (댓글)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS comments (
    comment_id   BIGINT       NOT NULL AUTO_INCREMENT,
    post_id      BIGINT       NOT NULL,
    user_id      BIGINT       NOT NULL,
    content      TEXT         NOT NULL,
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (comment_id),
    CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES posts (post_id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
