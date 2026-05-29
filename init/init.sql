-- =============================================================
-- Pass-IT Database Initialization Script
-- =============================================================

USE passit;

-- -------------------------------------------------------------
-- USERS (회원)
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
    CONSTRAINT chk_users_role
        CHECK (role IN ('USER', 'ADMIN'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- CERTIFICATIONS (자격증)
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
-- PROBLEM_SETS(문제 세트)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS problem_sets (
    problem_set_id BIGINT NOT NULL AUTO_INCREMENT,
    cert_id        BIGINT NOT NULL,
    user_id        BIGINT NOT NULL,

    PRIMARY KEY (problem_set_id),
    CONSTRAINT fk_problem_sets_cert_id
        FOREIGN KEY (cert_id)
        REFERENCES certifications (cert_id) ON DELETE CASCADE,
    CONSTRAINT fk_problem_sets_user_id
        FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- PROBLEMS (문제)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS problems (
    problem_id BIGINT NOT NULL AUTO_INCREMENT,
    problem_set_id BIGINT NOT NULL,
    problem_number INT NOT NULL COMMENT '문제 번호',
    problem_type VARCHAR(20) NOT NULL COMMENT 'MULTIPLE, SHORT_ANSWER',
    question TEXT NOT NULL COMMENT '문제',
    answer_correct_number INT NULL COMMENT '객관식일 경우 정답',
    answer_text TEXT NULL COMMENT '주관식일 경우 정답',

    PRIMARY KEY (problem_id),
    CONSTRAINT fk_problems_problem_set_id
        FOREIGN KEY (problem_set_id)
        REFERENCES problem_sets (problem_set_id) ON DELETE CASCADE,
    CONSTRAINT check_problem_type
        CHECK (problem_type IN ('MULTIPLE', 'SHORT_ANSWER')),
    CONSTRAINT check_problem_answer
        CHECK (answer_correct_number IS NOT NULL OR answer_text IS NOT NULL)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- PROBLEM_CHOICES(객관식 문제 보기)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS problem_choices (
    problem_choice_id BIGINT NOT NULL AUTO_INCREMENT,
    problem_id BIGINT NOT NULL,
    choice_number INT NOT NULL,
    content TEXT NOT NULL,

    PRIMARY KEY (problem_choice_id),
    UNIQUE KEY uq_problem_choices_number (problem_id, choice_number),
    CONSTRAINT fk_problem_choices_problem_id
        FOREIGN KEY (problem_id)
        REFERENCES problems (problem_id) ON DELETE CASCADE,
    CONSTRAINT check_choice_number_range
        CHECK (choice_number BETWEEN 1 AND 4)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- CODING_PROBLEMS (AI 생성 알고리즘 코딩 문제)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS coding_problems (
    problem_id         BIGINT       NOT NULL AUTO_INCREMENT,
    title              VARCHAR(255) NOT NULL,
    description        TEXT         NOT NULL,
    input_description  TEXT         NULL,
    output_description TEXT         NULL,
    constraint_text    TEXT         NULL,
    time_limit         INT          NOT NULL DEFAULT 1000 COMMENT '시간 제한 (ms)',
    memory_limit       INT          NOT NULL DEFAULT 256  COMMENT '메모리 제한 (MB)',
    difficulty         VARCHAR(10)  NULL COMMENT 'EASY / MEDIUM / HARD',
    category           VARCHAR(50)  NULL,
    created_at         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (problem_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- TEST_CASES (코딩 문제 테스트케이스)
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
    CONSTRAINT fk_test_cases_problem
        FOREIGN KEY (problem_id)
        REFERENCES coding_problems (problem_id) ON DELETE CASCADE,
    CONSTRAINT chk_test_cases_case_order
        CHECK (case_order >= 1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- SUBMISSIONS (사용자 코드 제출 기록)
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
    CONSTRAINT fk_submissions_problem
        FOREIGN KEY (problem_id)
        REFERENCES coding_problems (problem_id) ON DELETE CASCADE,
    CONSTRAINT fk_submissions_user
        FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT chk_submissions_language
        CHECK (language IN ('PYTHON', 'JAVA', 'CPP')),
    CONSTRAINT chk_submissions_status
        CHECK (status IN ('PENDING', 'RUNNING', 'DONE', 'FAILED')),
    CONSTRAINT chk_submissions_verdict
        CHECK (verdict IS NULL OR verdict IN ('AC', 'WA', 'CE', 'RE', 'TLE', 'MLE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- POSTS (게시글)
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
    CONSTRAINT fk_posts_user
        FOREIGN KEY (user_id)
        REFERENCES users (user_id),
    CONSTRAINT fk_posts_cert
        FOREIGN KEY (cert_id)
        REFERENCES certifications (cert_id),
    CONSTRAINT chk_posts_category
        CHECK (category IN ('REVIEW', 'TIP', 'QNA'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- COMMENTS (댓글)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS comments (
    comment_id   BIGINT       NOT NULL AUTO_INCREMENT,
    post_id      BIGINT       NOT NULL,
    user_id      BIGINT       NOT NULL,
    content      TEXT         NOT NULL,
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (comment_id),
    CONSTRAINT fk_comments_post
        FOREIGN KEY (post_id)
        REFERENCES posts (post_id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_user
        FOREIGN KEY (user_id)
        REFERENCES users (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
