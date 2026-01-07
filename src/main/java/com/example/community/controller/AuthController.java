package com.example.community.controller;

import com.example.community.common.constants.MessageConstants;
import com.example.community.common.dto.ApiResponse;
import com.example.community.dto.TokenResponse;
import com.example.community.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 인증 관련 API 컨트롤러 (토큰 갱신, 로그아웃 등)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Access Token 갱신
     * POST /api/auth/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        TokenResponse tokenResponse = authService.refreshAccessToken(refreshToken);

        ApiResponse<TokenResponse> response = ApiResponse.success(
                "토큰이 갱신되었습니다.",
                tokenResponse
        );

        return ResponseEntity.ok(response);
    }

    /**
     * 로그아웃 (RefreshToken 무효화)
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        authService.logout(refreshToken);

        ApiResponse<Void> response = ApiResponse.success("로그아웃되었습니다.");

        return ResponseEntity.ok(response);
    }
}
