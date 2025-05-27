package com.example.community.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegisterDto {

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @NotBlank(message = "닉네임은 필수입니다")
    @Size(min = 2, max = 20, message = "닉네임은 2-30자 사이여야 합니다")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 20, message = "비밀번호는 8-20자 사이여야 합니다")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다")
    private String confirmPassword;

    @Size(max = 500, message = "자기소개는 500자 이하여야 합니다")
    private String bio;

    @Size(max = 100, message = "GitHub URL은 100자 이하여야 합니다")
    private String githubUrl;

    // 기본 생성자
    public UserRegisterDto() {}

    // 생성자 - 필수인 정보들 반드시 포함
    public UserRegisterDto(String email, String nickname, String password, String confirmPassword) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    // Getter, Setter
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }

    // 비밀번호 일치 확인 - 암호화 비밀번호 검증
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}
