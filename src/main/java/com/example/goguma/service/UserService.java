package com.example.goguma.service;

import com.example.goguma.model.User;
import com.example.goguma.dto.CheckRequestDto;
import com.example.goguma.repository.UserRepository;
import com.example.goguma.dto.SignupRequestDto;
import com.example.goguma.security.kakao.KakaoOAuth2;
import com.example.goguma.security.kakao.KakaoUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final KakaoOAuth2 kakaoOAuth2;
    private final AuthenticationManager authenticationManager;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, KakaoOAuth2 kakaoOAuth2, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kakaoOAuth2 = kakaoOAuth2;
        this.authenticationManager = authenticationManager;
    }


    public void registerUser(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String nickname = requestDto.getNickname();

        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }
        //닉네임 중복 확인
        Optional<User> foundNick = userRepository.findByNickname(nickname);
        if (foundNick.isPresent()) {
            throw new IllegalArgumentException("현재 닉네임이 존재합니다.");
        }

        // 패스워드 인코딩
        String password = passwordEncoder.encode(requestDto.getPassword());
        String address = requestDto.getAddress();


        User user = new User(username, password, nickname, address);
        userRepository.save(user);
    }

    public void kakaoLogin(String authorizedCode) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        Long kakaoId = userInfo.getId();
        String kakaoNickname = userInfo.getNickname();
        String email = userInfo.getEmail();
        String profilePic = userInfo.getProfilePic();

        // 우리 DB 에서 회원 Id 와 패스워드
        // 회원 Id = 카카오 email
        String username = email;
        // 회원 닉네임 = 카카오 nickname
        String nickname = kakaoNickname;
        // 패스워드 = 카카오 Id + ADMIN TOKEN
        String password = email.substring(0,email.indexOf("@"));

        // DB 에 중복된 Kakao Id 가 있는지 확인
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);

        // 카카오 정보로 회원가입
        if (kakaoUser == null) {
            // 패스워드 인코딩
            String encodedPassword = passwordEncoder.encode(password);

            kakaoUser = new User(username, encodedPassword, nickname, kakaoId, profilePic);
            userRepository.save(kakaoUser);
        }

        // 로그인 처리
        Authentication kakaoUsernamePassword = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(kakaoUsernamePassword);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    //아이디, 닉네임 중복 체크
    public int checkUser(CheckRequestDto requestDto) {
        String username = requestDto.getUsername();
        Optional<User> found = userRepository.findByUsername(username);
        int count = 0;
        if (found.isPresent()) {
            count += 1;
        } else {
            count = 0;
        }
        return count;
    }

    public int checkNickname (CheckRequestDto requestDto) {
        String nickname = requestDto.getNickname();
        Optional<User> foundNick = userRepository.findByNickname(nickname);
        int count = 0;
        if (foundNick.isPresent()) {
            count += 1;
        } else {
            count = 0;
        }
        return count;
    }

    public Optional<User> profile(Long id){
        Optional<User> found = userRepository.findById(id);
        return found;
    }

}
