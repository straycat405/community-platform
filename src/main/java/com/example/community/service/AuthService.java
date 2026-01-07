package com.example.community.service;

import com.example.community.dto.TokenResponse;
import com.example.community.entity.RefreshToken;
import com.example.community.repository.RefreshTokenRepository;
import com.example.community.security.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 관련 서비스 (토큰 갱신, 로그아웃 등)
 */
@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Access Token 갱신
     */
    @Transactional
    public TokenResponse refreshAccessToken(String refreshToken) {
        // 1. RefreshToken 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 RefreshToken입니다.");
        }

        // 2. DB에서 RefreshToken 조회
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("RefreshToken을 찾을 수 없습니다."));

        // 3. 만료 확인
        if (storedToken.isExpired()) {
            refreshTokenRepository.delete(storedToken);
            throw new IllegalArgumentException("만료된 RefreshToken입니다.");
        }

        // 4. 새로운 AccessToken 생성
        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        Long userId = storedToken.getUserId();
        String newAccessToken = jwtTokenProvider.createAccessToken(email, userId);

        return new TokenResponse(newAccessToken, refreshToken);
    }

    /**
     * 로그아웃 (RefreshToken 무효화)
     */
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }
}
