package com.example.community.dto;

import com.example.community.entity.User;
import java.time.LocalDateTime;

// 응답 목적의 Dto는 이미 백엔드에서 검증된 db 데이터를 외부로 전달하므로 Annotation (Spring @Valid)를 활용한 추가 검증 불필요
public class UserResponseDto {

    private Long id;
    private String email;
    private String nickname;
    private String profileImage;
    private String bio;
    private String githubUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 기본 생성자
    public UserResponseDto() {}

    // User 엔티티로부터 생성하는 정적 팩토리 메서드
    public static UserResponseDto from(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.id = user.getId();
        dto.email = user.getEmail();
        dto.nickname = user.getNickname();
        dto.profileImage = user.getProfileImage();
        dto.bio = user.getBio();
        dto.githubUrl = user.getGithubUrl();
        dto.createdAt = user.getCreatedAt();
        dto.updatedAt = user.getUpdatedAt();
        return dto;
    }

    // Getter, Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}