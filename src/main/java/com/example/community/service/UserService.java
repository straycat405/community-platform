package com.example.community.service;

import com.example.community.common.constants.MessageConstants;
import com.example.community.dto.UserLoginDto;
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
            throw new IllegalArgumentException(MessageConstants.User.PASSWORD_MISMATCH);
        }

        // 2. 이메일 중복 확인
        if(userRepository.existsByEmail(registerDto.getEmail())) {
            throw new IllegalArgumentException(MessageConstants.User.EMAIL_ALREADY_EXISTS);
        }

        // 3. 닉네임 중복 확인
        if(userRepository.existsByNickname(registerDto.getNickname())) {
            throw new IllegalArgumentException(MessageConstants.User.NICKNAME_ALREADY_EXISTS);
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

    /**
     * 로그인 (비밀번호 확인)
     */
    public UserResponseDto login(UserLoginDto loginDto) {
        // 1. 이메일로 사용자 찾기
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.User.EMAIL_NOT_FOUND));

        // 2. 비밀번호 찾기
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(MessageConstants.User.PASSWORD_MISMATCH);
        }
        // 둘다 합격하면 로그인 통과
        // 3. DTO 변환 및 반환
        return UserResponseDto.from(user);
    }

    /**
     * 사용자 정보 조회 (findById)
     */
    public UserResponseDto getUserById(Long userId) {
        // id값으로 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.User.NOT_FOUND));

        return UserResponseDto.from(user);
    }

    /**
     * 사용자 정보 조회 (findByEmail)
     */
    public UserResponseDto getUserByEmail(String email) {
        // id값으로 유저 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.User.NOT_FOUND));

        return UserResponseDto.from(user);
    }

    /**
     * 이메일 중복 확인
     */
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 닉네임 중복 확인
     */
    public boolean isNicknameExists(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /**
     * 프로필 업데이트
     */
    @Transactional
    public UserResponseDto updateProfile(Long userId, String nickname, String bio, String githubUrl) {
        // 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.User.NOT_FOUND));

        // 2. 닉네임 중복 확인 (현재 사용자 제외)
        if (!user.getNickname().equals(nickname) && userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException(MessageConstants.User.NICKNAME_ALREADY_EXISTS);
        }

        // 3. 프로필 업데이트
        user.updateProfile(nickname, bio, githubUrl);

        // 4. 저장 (더티 체킹으로 자동 업데이트)
        return UserResponseDto.from(user);
    }




}
