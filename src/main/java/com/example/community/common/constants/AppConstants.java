package com.example.community.common.constants;

/**
 * 애플리케이션 설정 상수들
 */
public final class AppConstants {

    private AppConstants() {}

    /**
     * 사용자 관련 설정
     */
    public static final class User {
        public static final int NICKNAME_MIN_LENGTH = 2;
        public static final int NICKNAME_MAX_LENGTH = 20;
        public static final int PASSWORD_MIN_LENGTH = 8;
        public static final int PASSWORD_MAX_LENGTH = 20;
        public static final int BIO_MAX_LENGTH = 500;
        public static final int GITHUB_URL_MAX_LENGTH = 100;
        public static final int EMAIL_MAX_LENGTH = 100;

        private User() {}
    }

    /**
     * 게시글 관련 설정 (향후 추가)
     */
    public static final class Post {
        public static final int TITLE_MAX_LENGTH = 200;
        public static final int CONTENT_MAX_LENGTH = 10000;
        public static final int POSTS_PER_PAGE = 20;

        private Post() {}
    }

    /**
     * 파일 업로드 관련 설정
     */
    public static final class File {
        public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
        public static final String[] ALLOWED_IMAGE_EXTENSIONS = {"jpg", "jpeg", "png", "gif"};
        public static final String UPLOAD_DIR = "/uploads";
        public static final String PROFILE_IMAGE_DIR = "/uploads/profiles";

        private File() {}
    }

    /**
     * API 관련 설정
     */
    public static final class Api {
        public static final String BASE_URL = "/api";
        public static final String USER_URL = BASE_URL + "/users";
        public static final String POST_URL = BASE_URL + "/posts";
        public static final String COMMENT_URL = BASE_URL + "/comments";

        private Api() {}
    }

    /**
     * 정규표현식 패턴
     */
    public static final class Pattern {
        public static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        public static final String PHONE_PATTERN = "^010-\\d{4}-\\d{4}$";

        private Pattern() {}
    }
}