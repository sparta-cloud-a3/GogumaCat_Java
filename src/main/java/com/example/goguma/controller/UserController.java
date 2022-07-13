package com.example.goguma.controller;

import com.example.goguma.dto.*;
import com.example.goguma.service.AuthService;
import com.example.goguma.service.LikeService;
import com.example.goguma.service.PostService;
import com.example.goguma.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final LikeService likeService;
    private final PostService postService;

    //회원 로그인
    @PostMapping("/user/login1")
    @ResponseBody
    public String login(@RequestBody @Valid UserRequestDto.LoginDto loginDto) { //@Valid는 LoginDto에 모든 칸이 채워졌는지 검사
        return authService.login(loginDto);
    }

    // 회원 로그인 페이지
    @GetMapping("/user/login")
    public String login() {
        return "login";
    }

    // 회원 가입 페이지
    @GetMapping("/user/signup")
    public String signup() {
        return "signup";
    }

    // 회원 가입 요청 처리
    @PostMapping("/user/signup")
    public String registerUser(SignupRequestDto requestDto) {
        userService.registerUser(requestDto);
        return "redirect:/";
    }

    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(String code, HttpServletResponse httpServletResponse) {
        // authorizedCode: 카카오 서버로부터 받은 인가 코드
        String jwt = userService.kakaoLogin(code);
        //카카오 로그인 시 발급 받은 토큰으로 쿠키 만들어서 클라이언트로 넘겨주는 코드
        Cookie cookie = new Cookie("mytoken",jwt);
        cookie.setMaxAge(60*60*12); //토큰 12시간 지속
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

        return "redirect:/";
    }

    @ResponseBody
    @PostMapping ("/user/sign_up/check_dup")
    public int checkUser(CheckRequestDto requestDto){
        int result = userService.checkUser(requestDto);
        return result;
    }

    @ResponseBody
    @PostMapping("/user/sign_up/check_dup_nick")
    public int checkNickname (CheckRequestDto requestDto){
        int result = userService.checkNickname(requestDto);
        return result;
    }

    /**
     * user가 작성한 게시물 리스팅
     * @param userId
     * @return 작성한 게시물 리스트
     */
    @ResponseBody
    @GetMapping("/user/get_write_posts/{userId}")
    public List<PostResponseDto> getMyPosts(@PathVariable Long userId) {
        return userService.getMyPosts(userId);
    }

    /**
     * user의 관심 상품 리스팅
     * @param userId
     * @return 관심 상품 리스트
     */
    @ResponseBody
    @GetMapping("/user/get_like_posts/{userId}")
    public List<PostResponseDto> getLikePosts(@PathVariable Long userId) {
        return userService.getLikePosts(userId);
    }


    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public Long deleteUser(@PathVariable Long id){
       likeService.deleteLike(id);
       userService.delete(id);
       return id;
    }
}
