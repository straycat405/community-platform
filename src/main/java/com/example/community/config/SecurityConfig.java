package com.example.community.config;

import com.example.community.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 클래스가 설정 파일임을 Spring에게 알림
@Configuration
// Spring Security 기능 활성화
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // 패스워드 암호화를 위한 PasswordEncoder Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring이 해당 메서드를 실행해서 보안 설정을 만듬 (핵심 보안 체인)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 함수형 설정 패턴
        http
                // Url 접근 권한 설정
                .authorizeHttpRequests(authz -> authz

                                .requestMatchers("/api/health/**").permitAll()

                                // API 엔드포인트 허용 (회원가입, 로그인 등)
                                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                                .requestMatchers("/api/users/check-email", "/api/users/check-nickname").permitAll()
                                .requestMatchers("/api/auth/refresh", "/api/auth/logout").permitAll()

                                // 정적 리소스 허용 (CSS, JS, 이미지 등)
                                .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()

                                // Spring Boot 기본 경로들 허용
                                .requestMatchers("/", "/home", "/error").permitAll()

                                // Favicon 허용
                                .requestMatchers("/favicon.ico").permitAll()

                                // 나머지는 인증 필요
                                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // JWT 사용으로 CSRF 불필요

                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(java.util.List.of("http://localhost:5173", "http://localhost:5174", "http://localhost:5175"));
                    corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))

                // 세션 사용 안함 (JWT 사용)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // JWT 필터 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // H2 콘솔을 위한 헤더 설정 (Spring Boot 3.3+ 방식)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // H2 콘솔 iframe 허용
                        .httpStrictTransportSecurity(hstsConfig -> hstsConfig.disable()) // (개발단계) HSTS 비활성화
                );

        return http.build();
    }
}