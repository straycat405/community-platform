package com.example.community.entity;

import com.example.community.common.constants.AppConstants;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = AppConstants.User.EMAIL_MAX_LENGTH)
    private String email;

    @Column(nullable = false, length = AppConstants.User.NICKNAME_MAX_LENGTH)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(length = 500)
    private String profileImage;

    @Column(length = AppConstants.User.BIO_MAX_LENGTH)
    private String bio; //자기소개

    @Column(length = AppConstants.User.GITHUB_URL_MAX_LENGTH)
    private String githubUrl;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 기본 생성자 (JPA필수)
    protected User() {}

    // 생성자 - 필수 정보만 받음 , 필수 값 보장 (null 방지)
    public User(String email, String nickname, String password) {
        this.email=email;
        this.nickname=nickname;
        this.password=password;
    }

    // Getter
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getNickname() { return nickname; }
    public String getPassword() { return password; }
    public String getProfileImage() { return profileImage; }
    public String getBio() { return bio; }
    public String getGithubUrl() { return githubUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setter (필요한 것만) - 필수 정보 유지를 위한 분리
    public void updateProfile(String nickname, String bio, String githubUrl) {
        this.nickname = nickname;
        this.bio = bio;
        this.githubUrl = githubUrl;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
