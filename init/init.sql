-- =============================================================
-- Pass-IT Database Initialization Script
-- =============================================================

USE passit;

-- -------------------------------------------------------------
-- 1. USERS (회원)
--    FK 없음 — 가장 먼저 생성
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
    UNIQUE KEY uq_users_nickname (nickname)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- 2. CERTIFICATIONS (자격증)
--    FK 없음 — PROBLEMS·POSTS보다 먼저 생성
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS certifications (
    cert_id BIGINT      NOT NULL AUTO_INCREMENT,
    name    VARCHAR(100) NOT NULL,

    PRIMARY KEY (cert_id),
    UNIQUE KEY uq_certifications_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 초기 자격증 데이터
INSERT INTO certifications (name) VALUES
    ('정보처리기사'),
    ('SQLD');

-- -------------------------------------------------------------
-- 3. PROBLEMS (문제)
--    FK: cert_id → certifications, author_id → users
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS problems (
    problem_id    BIGINT        NOT NULL AUTO_INCREMENT,
    cert_id       BIGINT        NOT NULL,
    author_id     BIGINT        NOT NULL,
    title         VARCHAR(255)  NOT NULL,
    description   TEXT          NOT NULL,
    time_limit    INT           NOT NULL DEFAULT 5000   COMMENT '제한 시간 (ms)',
    memory_limit  INT           NOT NULL DEFAULT 256    COMMENT '메모리 제한 (MB)',
    source_type   VARCHAR(10)   NOT NULL                COMMENT 'AI / USER / EXAM',
    status        VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    created_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (problem_id),
    CONSTRAINT fk_problems_cert    FOREIGN KEY (cert_id)   REFERENCES certifications (cert_id),
    CONSTRAINT fk_problems_author  FOREIGN KEY (author_id) REFERENCES users (user_id),
    CONSTRAINT chk_problems_source_type CHECK (source_type IN ('AI', 'USER', 'EXAM')),
    CONSTRAINT chk_problems_status      CHECK (status      IN ('PENDING', 'APPROVED', 'REJECTED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- 4. POSTS (게시글)
--    FK: user_id → users, cert_id → certifications
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS posts (
    post_id    BIGINT       NOT NULL AUTO_INCREMENT,
    user_id    BIGINT       NOT NULL,
    cert_id    BIGINT       NOT NULL,
    category   VARCHAR(20)  NOT NULL COMMENT 'REVIEW / TIP / QNA',
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    view_count INT          NOT NULL DEFAULT 0,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (post_id),
    CONSTRAINT fk_posts_user FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_posts_cert FOREIGN KEY (cert_id) REFERENCES certifications (cert_id),
    CONSTRAINT chk_posts_category CHECK (category IN ('REVIEW', 'TIP', 'QNA'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- 5. COMMENTS (댓글)
--    FK: post_id → posts, user_id → users
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS comments (
    comment_id BIGINT   NOT NULL AUTO_INCREMENT,
    post_id    BIGINT   NOT NULL,
    user_id    BIGINT   NOT NULL,
    content    TEXT     NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (comment_id),
    CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES posts (post_id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
