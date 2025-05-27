package com.example.community.dto;

import com.example.community.common.constants.AppConstants;
import com.example.community.common.constants.MessageConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegisterDto {

    @NotBlank(message = MessageConstants.Validation.EMAIL_REQUIRED)
    @Email(message = MessageConstants.Validation.EMAIL_INVALID_FORMAT)
    private String email;

    @NotBlank(message = MessageConstants.Validation.NICKNAME_REQUIRED)
    @Size(min = AppConstants.User.NICKNAME_MIN_LENGTH,
            max = AppConstants.User.NICKNAME_MAX_LENGTH,
            message = MessageConstants.Validation.NICKNAME_SIZE_INVALID)
    private String nickname;

    @NotBlank(message = MessageConstants.Validation.PASSWORD_REQUIRED)
    @Size(min = AppConstants.User.PASSWORD_MIN_LENGTH,
            max = AppConstants.User.PASSWORD_MAX_LENGTH,
            message = MessageConstants.Validation.PASSWORD_SIZE_INVALID)
    private String password;

    @NotBlank(message = MessageConstants.Validation.PASSWORD_CONFIRM_REQUIRED)
    private String confirmPassword;

    @Size(max = AppConstants.User.BIO_MAX_LENGTH,
            message = MessageConstants.Validation.BIO_SIZE_INVALID)
    private String bio;

    @Size(max = AppConstants.User.GITHUB_URL_MAX_LENGTH,
            message = MessageConstants.Validation.GITHUB_URL_SIZE_INVALID)
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
