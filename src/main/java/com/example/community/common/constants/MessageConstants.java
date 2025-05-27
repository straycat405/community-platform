package com.example.community.common.constants;

/**
 * 메시지 상수들을 카테고리별로 관리하는 클래스
 */
public final class MessageConstants {

    private MessageConstants() {}

    /**
     * API 공통 응답 메시지
     */
    public static final class Api {
        public static final String SUCCESS = "성공";
        public static final String ERROR = "오류가 발생했습니다";
        public static final String INTERNAL_SERVER_ERROR = "서버 오류가 발생했습니다";
        public static final String BAD_REQUEST = "잘못된 요청입니다";
        public static final String UNAUTHORIZED = "인증이 필요합니다";
        public static final String FORBIDDEN = "접근 권한이 없습니다";
        public static final String NOT_FOUND = "요청한 리소스를 찾을 수 없습니다";

        private Api() {}
    }

    /**
     * 사용자 관련 메시지
     */
    public static final class User {
        public static final String REGISTER_SUCCESS = "회원가입이 완료되었습니다";
        public static final String LOGIN_SUCCESS = "로그인이 완료되었습니다";
        public static final String NOT_FOUND = "존재하지 않는 사용자입니다";
        public static final String EMAIL_NOT_FOUND = "존재하지 않는 이메일입니다";
        public static final String EMAIL_ALREADY_EXISTS = "이미 존재하는 이메일입니다";
        public static final String NICKNAME_ALREADY_EXISTS = "이미 존재하는 닉네임입니다";
        public static final String PASSWORD_MISMATCH = "비밀번호가 일치하지 않습니다";
        public static final String EMAIL_AVAILABLE = "사용 가능한 이메일입니다";
        public static final String EMAIL_UNAVAILABLE = "이미 사용 중인 이메일입니다";
        public static final String NICKNAME_AVAILABLE = "사용 가능한 닉네임입니다";
        public static final String NICKNAME_UNAVAILABLE = "이미 사용 중인 닉네임입니다";
        public static final String PROFILE_UPDATE_SUCCESS = "프로필이 업데이트되었습니다";

        private User() {}
    }

    /**
     * 유효성 검증 메시지
     */
    public static final class Validation {
        public static final String EMAIL_REQUIRED = "이메일은 필수입니다";
        public static final String EMAIL_INVALID_FORMAT = "올바른 이메일 형식이 아닙니다";
        public static final String PASSWORD_REQUIRED = "비밀번호는 필수입니다";
        public static final String PASSWORD_CONFIRM_REQUIRED = "비밀번호 확인은 필수입니다";
        public static final String NICKNAME_REQUIRED = "닉네임은 필수입니다";
        public static final String NICKNAME_SIZE_INVALID = "닉네임은 2-20자 사이여야 합니다";
        public static final String PASSWORD_SIZE_INVALID = "비밀번호는 8-20자 사이여야 합니다";
        public static final String BIO_SIZE_INVALID = "자기소개는 500자 이하여야 합니다";
        public static final String GITHUB_URL_SIZE_INVALID = "GitHub URL은 100자 이하여야 합니다";

        private Validation() {}
    }

    /**
     * 게시글 관련 메시지 (향후 추가될 예정)
     */
    public static final class Post {
        public static final String CREATE_SUCCESS = "게시글이 작성되었습니다";
        public static final String UPDATE_SUCCESS = "게시글이 수정되었습니다";
        public static final String DELETE_SUCCESS = "게시글이 삭제되었습니다";
        public static final String NOT_FOUND = "존재하지 않는 게시글입니다";

        private Post() {}
    }

    /**
     * 댓글 관련 메시지 (향후 추가될 예정)
     */
    public static final class Comment {
        public static final String CREATE_SUCCESS = "댓글이 작성되었습니다";
        public static final String UPDATE_SUCCESS = "댓글이 수정되었습니다";
        public static final String DELETE_SUCCESS = "댓글이 삭제되었습니다";
        public static final String NOT_FOUND = "존재하지 않는 댓글입니다";

        private Comment() {}
    }
}