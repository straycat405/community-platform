package com.example.community.dto;

/**
 * 로그인 성공 응답 DTO
 * 사용자 정보 + 토큰 정보
 */
public class LoginResponse {
    private UserResponseDto user;
    private TokenResponse tokens;

    public LoginResponse(UserResponseDto user, TokenResponse tokens) {
        this.user = user;
        this.tokens = tokens;
    }

    // Getters
    public UserResponseDto getUser() { return user; }
    public TokenResponse getTokens() { return tokens; }
}
