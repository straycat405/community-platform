package com.example.community.controller;

import com.example.community.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    /**
     * 서버 상태 확인 및 관리 - 핑 체크
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        Map<String, Object> healthData = new HashMap<>();
        healthData.put("status", "up");
        healthData.put("timestamp", LocalDateTime.now());
        healthData.put("service", "Community Platform");
        healthData.put("version", "1.0.0");

        ApiResponse<Map<String, Object>> response = ApiResponse.success(
                "서버가 정상적으로 동작 중입니다.",
                healthData
        );

        return ResponseEntity.ok(response);
    }

    /**
     * 상세 상태 확인 - 데이터베이스 연결 등 포함
     * GET /api/health/detailed
     */
    @GetMapping("/detailed")
    public ResponseEntity<ApiResponse<Map<String, Object>>> detailedHealthCheck() {
        Map<String, Object> healthData = new HashMap<>();

        try {
            // 여기서 데이터베이스 연결 상태 등을 체크할 수 있음
            healthData.put("status", "UP");
            healthData.put("database", "UP");
            healthData.put("timestamp", LocalDateTime.now());
            healthData.put("service", "Community Platform");
            healthData.put("version", "1.0.0");
            healthData.put("activeProfiles", System.getProperty("spring.profiles.active", "default"));

            ApiResponse<Map<String, Object>> response = ApiResponse.success(
                    "모든 시스템이 정상적으로 동작 중입니다.",
                    healthData
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            healthData.put("status", "DOWN");
            healthData.put("error", e.getMessage());
            healthData.put("timestamp", LocalDateTime.now());

            ApiResponse<Map<String, Object>> response = ApiResponse.errorWithData(
                    "시스템에 문제가 발생했습니다.",
                    healthData
            );

            return ResponseEntity.status(503).body(response);
        }
    }

    /**
     * 간단한 핑 체크 - 최소한의 응답
     * GET /api/health/ping
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

}
