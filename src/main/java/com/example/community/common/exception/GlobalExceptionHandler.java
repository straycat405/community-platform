package com.example.community.common.exception;

import com.example.community.common.constants.MessageConstants;
import com.example.community.common.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리 핸들러
 * 모든 컨트롤러에서 발생하는 예외를 통일된 방식으로 처리
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * IllegalArgumentException 처리
     * 비즈니스 로직 검증 실패시 발생
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("비즈니스 로직 검증 실패: {}", e.getMessage());

        ApiResponse<Object> response = ApiResponse.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * MethodArgumentNotValidException 처리
     * @Valid 어노테이션 검증 실패시 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
        logger.warn("유효성 검증 실패: {}", e.getMessage());

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Object> response = ApiResponse.error(MessageConstants.Api.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * RuntimeException 처리
     * 예상치 못한 런타임 에러
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException e) {
        logger.error("런타임 예외 발생: ", e);

        ApiResponse<Object> response = ApiResponse.error(MessageConstants.Api.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Exception 처리
     * 모든 예외의 최종 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception e) {
        logger.error("예상치 못한 예외 발생: ", e);

        ApiResponse<Object> response = ApiResponse.error(MessageConstants.Api.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 인증 관련 예외 처리 (향후 JWT 구현시 사용)
     */
    // @ExceptionHandler(AuthenticationException.class)
    // public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException e) {
    //     logger.warn("인증 실패: {}", e.getMessage());
    //
    //     ApiResponse<Object> response = ApiResponse.error(MessageConstants.Api.UNAUTHORIZED);
    //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    // }

    /**
     * 권한 관련 예외 처리 (향후 권한 시스템 구현시 사용)
     */
    // @ExceptionHandler(AccessDeniedException.class)
    // public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException e) {
    //     logger.warn("접근 권한 없음: {}", e.getMessage());
    //
    //     ApiResponse<Object> response = ApiResponse.error(MessageConstants.Api.FORBIDDEN);
    //     return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    // }
}