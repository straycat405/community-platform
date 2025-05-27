package com.example.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// 클래스가 설정 파일임을 Spring에게 알림
@Configuration
// Spring Security 기능 활성화
@EnableWebSecurity
public class SecurityConfig {

    // Spring이 해당 메서드를 실행해서 보안 설정을 만듬 (핵심 보안 체인)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 함수형 설정 패턴
        http
                // Url 접근 권한 설정
                .authorizeHttpRequests(authz -> authz
                                // H2 콘솔 접근 허용
                                .requestMatchers("/h2-console/**").permitAll()

                                // API 엔드포인트 허용
                                .requestMatchers("/api/**").permitAll()

                                // 정적 리소스 허용 (CSS, JS, 이미지 등)
                                .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()

                                // Spring Boot 기본 경로들 허용
                                .requestMatchers("/", "/home", "/error").permitAll()

                                // Favicon 허용
                                .requestMatchers("/favicon.ico").permitAll()

                                // 개발 단계에서 모든 요청 허용 (임시)
                                .anyRequest().permitAll()

                        // 실제 운영시에는 아래처럼 사용
                        // .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // (개발단계) CSRF 보호 비활성화

                // H2 콘솔을 위한 헤더 설정 (Spring Boot 3.3+ 방식)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // H2 콘솔 iframe 허용
                        .httpStrictTransportSecurity(hstsConfig -> hstsConfig.disable()) // (개발단계) HSTS 비활성화
                );

        return http.build();
    }
}