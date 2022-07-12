package com.example.goguma.controller;

import com.example.goguma.jwt.JwtProvider;
import com.example.goguma.model.User;
import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    @RequestMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User info = userDetails.getUser();
        model.addAttribute("id",info.getId());
        model.addAttribute("nickname", info.getNickname());
        return "index";
    }

    @GetMapping("/profileinfo/{id}")
    public String info(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User tokenInfo = userDetails.getUser();
        model.addAttribute("id",tokenInfo.getId());
        User info = userService.profile(id);
        model.addAttribute("pageUserId", info.getId());
        model.addAttribute("nickname", info.getNickname());
        model.addAttribute("username", info.getUsername());
        model.addAttribute("profilePic", info.getProfilePic());
        model.addAttribute("kakaoId", info.getKakaoId());
        model.addAttribute("address", info.getAddress());
        model.addAttribute("profileInfo", info.getProfileInfo());
        return "user";
    }

    @GetMapping("/posting/{username}")
    public String posting(@PathVariable String username, Model model,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User info = userDetails.getUser();
        model.addAttribute("username", info.getUsername());
        return "posting";
    }
}
