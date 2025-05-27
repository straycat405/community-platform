package com.example.community.dto;

import com.example.community.common.constants.MessageConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserLoginDto {

    @NotBlank(message = MessageConstants.Validation.EMAIL_REQUIRED)
    @Email(message = MessageConstants.Validation.EMAIL_INVALID_FORMAT)
    private String email;

    @NotBlank(message = MessageConstants.Validation.PASSWORD_REQUIRED)
    private String password;

    // 기본 생성자
    public UserLoginDto() {}

    // 생성자
    public UserLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter, Setter
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}