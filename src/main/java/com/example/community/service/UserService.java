package com.example.community.service;

import com.example.community.dto.UserRegisterDto;
import com.example.community.dto.UserResponseDto;
import com.example.community.entity.User;
import com.example.community.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// @Transactional은 메서드 실행을 하나의 작업 단위로 묶음
// 작업 성공시 DB 반영 (commit)
// 작업 실패시 모든 변경사항 취소 (rollback)
// 추가로 읽기 전용 (select) 작업만 하므로 readOnly = true 설정 추가
@Service
@Transactional(readOnly = true)
public class UserService {

    // @Autowired 사용시
    // public UserRepository userRepository로 끝낼 수 있지만
    // 불변성 보장 안됨 (가변 필드)
    // final로 불변 보장하는 것이 안정적임 - 변형시 컴파일 단계에서 에러

    // @RequiredArgsConstructor 사용시
    // private final UserRepository userRepository; 선언으로 사용
    // 생성자는 자동 생성 , 편리하면서 안전함
    // 장기적으로는 생성자 주입은 직접 하는 것이 안전하다. 익숙해지도록 하자.

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 기본 생성자
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입
     */
    @Transactional
    public UserResponseDto register(UserRegisterDto registerDto) {
        // 1. 비밀번호 일치 확인
        if(!registerDto.isPasswordMatching()) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        // 2. 이메일 중복 확인
        if(userRepository.existsByEmail(registerDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다");
        }

        // 3. 닉네임 중복 확인
        if(userRepository.existsByNickname(registerDto.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다");
        }

        // 4. 다 통과하면 받은 양식 중 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

        // 5. User 엔티티 생성
        User user = new User(
                registerDto.getEmail(),
                registerDto.getNickname(),
                encodedPassword
        );

        // 6. 선택적 (Optional 정보 저장)
        if (registerDto.getBio() != null || registerDto.getGithubUrl() != null) {
            user.updateProfile(
                    registerDto.getNickname(),
                    registerDto.getBio(),
                    registerDto.getGithubUrl()
            );
        }

        // 7. 최종 저장
        User savedUser = userRepository.save(user);

        // 8. DTO로 변환 및 반환
        return UserResponseDto.from(savedUser);
    }



}
