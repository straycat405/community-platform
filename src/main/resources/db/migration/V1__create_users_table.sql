-- 사용자 테이블 생성
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    nickname VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    profile_image VARCHAR(500),
    bio VARCHAR(500),
    github_url VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 이메일 검색을 위한 인덱스
CREATE INDEX idx_users_email ON users(email);

-- 생성일 검색을 위한 인덱스
CREATE INDEX idx_users_created_at ON users(created_at);
