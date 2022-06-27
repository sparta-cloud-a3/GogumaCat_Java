package com.example.goguma.controller;

import com.example.goguma.dto.CheckRequestDto;
import com.example.goguma.dto.PostResponseDto;
import com.example.goguma.dto.SignupRequestDto;
import com.example.goguma.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원 로그인 페이지
    @GetMapping("/user/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
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

    @GetMapping("/user/forbidden")
    public String forbidden() {
        return "forbidden";
    }

    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(String code) {
        // authorizedCode: 카카오 서버로부터 받은 인가 코드
        userService.kakaoLogin(code);

        return "redirect:/";
    }

    @ResponseBody
    @RequestMapping(value = "/user/sign_up/check_dup", method = RequestMethod.POST)
    public int checkUser(CheckRequestDto requestDto){
        int result = userService.checkUser(requestDto);
        System.out.println(result);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/user/sign_up/check_dup_nick", method = RequestMethod.POST)
    public int checkNickname (CheckRequestDto requestDto){
        int result = userService.checkNickname(requestDto);
        System.out.println(result);
        return result;
    }

    @ResponseBody
    @GetMapping("/user/get_posts/{userId}")
    public List<PostResponseDto> getMyPosts(@PathVariable Long userId) {
        return userService.getMyPosts(userId);
    }
}
