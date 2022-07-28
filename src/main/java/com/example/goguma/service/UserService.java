package com.example.goguma.service;

import com.example.goguma.dto.*;
import com.example.goguma.exception.NoSuchUserException;
import com.example.goguma.jwt.JwtProvider;
import com.example.goguma.model.Order;
import com.example.goguma.model.Post;
import com.example.goguma.model.User;
import com.example.goguma.repository.ChatRepository;
import com.example.goguma.repository.PostImgRepository;
import com.example.goguma.repository.UserRepository;
import com.example.goguma.security.kakao.KakaoOAuth2;
import com.example.goguma.security.kakao.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final KakaoOAuth2 kakaoOAuth2;
    private final PostImgRepository postImgRepository;
    private final ChatRepository chatRepository;
    private final JwtProvider jwtProvider;

    public Long registerUser(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String nickname = requestDto.getNickname();

        // 패스워드 인코딩
        String password = passwordEncoder.encode(requestDto.getPassword());
        String address = requestDto.getAddress();

        User user = new User(username, password, nickname, address);
        return userRepository.save(user).getId();
    }

    public String kakaoLogin(String authorizedCode) {
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
        String jwt = jwtProvider.createKakaoAccessToken(username);
        jwtProvider.createKakaoRefreshToken(username);

        return jwt;
    }

    //아이디, 닉네임 중복 체크
    public int checkUser(CheckUsernameRequestDto requestDto) {
        String username = requestDto.getUsername();
        Optional<User> found = userRepository.findByUsername(username);
        int count = 0;
        if (found.isPresent()) {
            count += 1;
        }

        return count;
    }

    public int checkNickname (CheckNicknameRequestDto requestDto) {
        String nickname = requestDto.getNickname();
        Optional<User> foundNick = userRepository.findByNickname(nickname);
        int count = 0;
        if (foundNick.isPresent()) {
            count += 1;
        }

        return count;
    }

    /**
     * 내가 작성한 게시물 가져오기
     * @param userId
     * @return List<PostResponseDto> posts: 작성한 게시물
     */
    public List<PostResponseDto> getMyPosts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                NoSuchUserException::new
        );

        List<PostResponseDto> posts = user.getPosts().stream().map(
                p -> {
                    return new PostResponseDto(
                            p.getId(), p.getTitle(), p.getPrice(), p.getAddress(), p.getLikeCount(), p.isSold()
                    );
                }
        ).collect(Collectors.toList());

        for (PostResponseDto post : posts) { //fetch type이 LAZY이기 때문에 하나씩 받아오기
            postImgRepository.findByPostId(post.getPostId()).forEach(
                    pi -> {
                        post.getPostImgs().add(new PostImgResponseDto(pi.getImg_url()));
                    }
            );
        }

        return posts;
    }

    public User profile(Long id){
        return userRepository.findById(id).orElseThrow(
                NoSuchUserException::new
        );
    }

    public List<PostResponseDto> getLikePosts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                NoSuchUserException::new
        );

        List<PostResponseDto> posts = user.getLikes().stream().map(
                l -> {
                    Post p = l.getPost();
                    return new PostResponseDto(
                            p.getId(), p.getTitle(), p.getPrice(), p.getAddress(), p.getLikeCount(), p.isSold()
                    );
                }
        ).collect(Collectors.toList());

        for (PostResponseDto post : posts) { //fetch type이 LAZY이기 때문에 하나씩 받아오기
            postImgRepository.findByPostId(post.getPostId()).forEach(
                    pi -> {
                        post.getPostImgs().add(new PostImgResponseDto(pi.getImg_url()));
                    }
            );
        }

        return posts;
    }

    @Transactional
    public String deleteUser(Long id){
        chatRepository.deleteByUserId(id);
        userRepository.deleteById(id);
        return "회원 탈퇴에 성공하였습니다.";
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(
                NoSuchUserException::new
        );
    }

    public List<PostResponseDto> getMyOrderPosts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                NoSuchUserException::new
        );
        List<PostResponseDto> posts = user.getOrders().stream().map(
                o -> {
                    Post p = o.getPost();
                    return new PostResponseDto(p.getId(), p.getTitle(), p.getPrice(), p.getAddress(), p.getLikeCount(), p.isSold());
                }
        ).collect(Collectors.toList());

        for (PostResponseDto post : posts) { //fetch type이 LAZY이기 때문에 하나씩 받아오기
            postImgRepository.findByPostId(post.getPostId()).forEach(
                    pi -> {
                        post.getPostImgs().add(new PostImgResponseDto(pi.getImg_url()));
                    }
            );
        }

        return posts;
    }
}
