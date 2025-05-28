package com.example.community.controller;

import com.example.community.common.constants.AppConstants;
import com.example.community.common.constants.MessageConstants;
import com.example.community.dto.UserLoginDto;
import com.example.community.dto.UserRegisterDto;
import com.example.community.dto.UserResponseDto;
import com.example.community.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(AppConstants.Api.USER_URL)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입
     * POST /api/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> register(@Valid @RequestBody UserRegisterDto registerDto) {
        try {
            UserResponseDto userResponse = userService.register(registerDto);

            Map<String,Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", MessageConstants.User.REGISTER_SUCCESS);
            response.put("data", userResponse);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * 로그인
     * POST /api/users/login
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserLoginDto loginDto) {
        try {
            UserResponseDto userResponse = userService.login(loginDto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", MessageConstants.User.LOGIN_SUCCESS);
            response.put("data", userResponse);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    /**
     * 사용자 정보 조회
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        try {
            UserResponseDto userResponse = userService.getUserById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", userResponse);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * 이메일 중복 확인
     * GET /api/users/check-email?email=test@example.com
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
        boolean exists = userService.isEmailExists(email);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("exists", exists);
        response.put("message", exists ?
                MessageConstants.User.EMAIL_UNAVAILABLE :
                MessageConstants.User.EMAIL_AVAILABLE);

        return ResponseEntity.ok(response);
    }

    /**
     * 닉네임 중복 확인
     * GET /api/users/check-nickname?nickname=testuser
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Object>> checkNickname(@RequestParam String nickname) {
        boolean exists = userService.isNicknameExists(nickname);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("exists", exists);
        response.put("message", exists ?
                MessageConstants.User.NICKNAME_UNAVAILABLE :
                MessageConstants.User.NICKNAME_AVAILABLE);

        return ResponseEntity.ok(response);
    }

    /**
     * 프로필 업데이트
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProfile(
            @PathVariable Long id,
            @RequestBody Map<String, String> updateData) {
        try {
            String nickname = updateData.get("nickname");
            String bio = updateData.get("bio");
            String githubUrl = updateData.get("githubUrl");

            UserResponseDto userResponse = userService.updateProfile(id, nickname, bio, githubUrl);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", MessageConstants.User.PROFILE_UPDATE_SUCCESS);
            response.put("data", userResponse);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * 이메일로 사용자 정보 조회 (관리용)
     * GET /api/users/by-email?email=test@example.com
     */
    @GetMapping("/by-email")
    public ResponseEntity<Map<String, Object>> getUserByEmail(@RequestParam String email) {
        try {
            UserResponseDto userResponse = userService.getUserByEmail(email);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", userResponse);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
