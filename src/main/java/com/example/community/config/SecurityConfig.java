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
                        .requestMatchers("/h2-console/**").permitAll() //H2 콘솔은 누구나 접근 가능
                        .requestMatchers("/api/**").permitAll() //API 누구나 접근 가능
                        .anyRequest().authenticated() // 그 외에는 로그인 필요
                )
                .csrf(csrf -> csrf.disable()) // (개발단계) csrf 보호 비활성화
                .headers(headers -> headers
                        .httpStrictTransportSecurity(hstsConfig -> hstsConfig.disable()) //(개발단계) HSTS(HTTP Strict Transport Security) 헤더 비활성화
                );

        return http.build();
    }




}