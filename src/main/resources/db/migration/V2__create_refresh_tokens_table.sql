-- RefreshToken 테이블 생성
CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(500) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 토큰 검색을 위한 인덱스
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);

-- 사용자별 토큰 조회를 위한 인덱스
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);

-- 만료된 토큰 정리를 위한 인덱스
CREATE INDEX idx_refresh_tokens_expiry_date ON refresh_tokens(expiry_date);
