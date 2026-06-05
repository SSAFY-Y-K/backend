INSERT INTO certifications (name) VALUES
    ('정보처리기사'),
    ('SQLD');

INSERT INTO users (
    user_id,
    login_id,
    password,
    nickname,
    role,
    created_at,
    updated_at
) VALUES (
    1,
    'admin',
    '{noop}admin1234',
    '관리자',
    'ADMIN',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO problem_sets (
    problem_set_id,
    cert_id,
    user_id
) VALUES (
    1,
    1,
    1
);

INSERT INTO problems (
    problem_id,
    problem_set_id,
    problem_number,
    problem_type,
    question,
    answer_correct_number,
    answer_text
) VALUES (
    1,
    1,
    1,
    'MULTIPLE',
    'SQL의 기본 키(primary key)는 무엇인가?',
    NULL,
    '테이블의 각 행을 유일하게 식별하는 키'
);

INSERT INTO problem_choices (
    problem_choice_id,
    problem_id,
    choice_number,
    content
) VALUES
    (1, 1, 1, '식별자'),
    (2, 1, 2, '인덱스'),
    (3, 1, 3, '외래 키'),
    (4, 1, 4, '정규화');

INSERT INTO test_cases (
    test_case_id,
    problem_id,
    input_data,
    expected_output,
    is_sample,
    case_order,
    created_at,
    updated_at
) VALUES
    (1, 1, 'sample input\n', 'sample output\n', TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO posts (
    post_id,
    user_id,
    cert_id,
    category,
    title,
    content,
    view_count,
    created_at,
    updated_at
) VALUES (
    1,
    1,
    1,
    'QNA',
    '초기 데이터',
    'init.sql 기준으로 H2를 초기화합니다.',
    0,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO comments (
    comment_id,
    post_id,
    user_id,
    content,
    created_at,
    updated_at
) VALUES (
    1,
    1,
    1,
    '초기 코멘트',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);
