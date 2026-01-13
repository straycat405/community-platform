package com.example.community.controller;

import com.example.community.common.constants.AppConstants;
import com.example.community.common.constants.MessageConstants;
import com.example.community.common.dto.ApiResponse;
import com.example.community.dto.LoginResponse;
import com.example.community.dto.UserLoginDto;
import com.example.community.dto.UserRegisterDto;
import com.example.community.dto.UserResponseDto;
import com.example.community.security.JwtTokenProvider;
import com.example.community.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 사용자 관련 REST API 컨트롤러
 * GlobalExceptionHandler가 예외를 처리하므로 try-catch 제거
 */
@RestController
@RequestMapping(AppConstants.Api.USER_URL)
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 회원가입
     * POST /api/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> register(@Valid @RequestBody UserRegisterDto registerDto) {
        UserResponseDto userResponse = userService.register(registerDto);

        ApiResponse<UserResponseDto> response = ApiResponse.success(
                MessageConstants.User.REGISTER_SUCCESS,
                userResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 로그인
     * POST /api/users/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody UserLoginDto loginDto) {
        LoginResponse loginResponse = userService.login(loginDto);

        ApiResponse<LoginResponse> response = ApiResponse.success(
                MessageConstants.User.LOGIN_SUCCESS,
                loginResponse
        );

        return ResponseEntity.ok(response);
    }

    /**
     * 현재 로그인한 사용자 정보 조회 (JWT 토큰 기반)
     * GET /api/users/me
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getCurrentUser(HttpServletRequest request) {
        // Authorization 헤더에서 토큰 추출
        String token = extractTokenFromRequest(request);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("토큰이 없습니다."));
        }

        // 토큰에서 사용자 ID 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        UserResponseDto userResponse = userService.getUserById(userId);

        ApiResponse<UserResponseDto> response = ApiResponse.success(userResponse);
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자 정보 조회
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable Long id) {
        UserResponseDto userResponse = userService.getUserById(id);

        ApiResponse<UserResponseDto> response = ApiResponse.success(userResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * HTTP 요청에서 JWT 토큰 추출
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 이메일 중복 확인
     * GET /api/users/check-email?email=test@example.com
     */
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkEmail(@RequestParam String email) {
        boolean exists = userService.isEmailExists(email);

        Map<String, Object> data = new HashMap<>();
        data.put("exists", exists);
        data.put("message", exists ?
                MessageConstants.User.EMAIL_UNAVAILABLE :
                MessageConstants.User.EMAIL_AVAILABLE);

        ApiResponse<Map<String, Object>> response = ApiResponse.success(data);

        return ResponseEntity.ok(response);
    }

    /**
     * 닉네임 중복 확인
     * GET /api/users/check-nickname?nickname=testuser
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkNickname(@RequestParam String nickname) {
        boolean exists = userService.isNicknameExists(nickname);

        Map<String, Object> data = new HashMap<>();
        data.put("exists", exists);
        data.put("message", exists ?
                MessageConstants.User.NICKNAME_UNAVAILABLE :
                MessageConstants.User.NICKNAME_AVAILABLE);

        ApiResponse<Map<String, Object>> response = ApiResponse.success(data);

        return ResponseEntity.ok(response);
    }

    /**
     * 프로필 업데이트
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateProfile(
            @PathVariable Long id,
            @RequestBody Map<String, String> updateData) {

        String nickname = updateData.get("nickname");
        String bio = updateData.get("bio");
        String githubUrl = updateData.get("githubUrl");

        UserResponseDto userResponse = userService.updateProfile(id, nickname, bio, githubUrl);

        ApiResponse<UserResponseDto> response = ApiResponse.success(
                MessageConstants.User.PROFILE_UPDATE_SUCCESS,
                userResponse
        );

        return ResponseEntity.ok(response);
    }

    /**
     * 이메일로 사용자 정보 조회 (관리용)
     * GET /api/users/by-email?email=test@example.com
     */
    @GetMapping("/by-email")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserByEmail(@RequestParam String email) {
        UserResponseDto userResponse = userService.getUserByEmail(email);

        ApiResponse<UserResponseDto> response = ApiResponse.success(userResponse);

        return ResponseEntity.ok(response);
    }
}