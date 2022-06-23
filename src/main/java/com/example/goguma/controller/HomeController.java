package com.example.goguma.controller;

import com.example.goguma.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("nickname", userDetails.getNickname());
        return "index";
    }


    @GetMapping("/profileinfo/{nickname}")
    public String info(@PathVariable String nickname, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("nickname", userDetails.getNickname());
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("profilePic", userDetails.getProfilePic());
        model.addAttribute("kakaoId", userDetails.getKakaoId());
        model.addAttribute("address", userDetails.getAddress());
        model.addAttribute("profileInfo", userDetails.getProfileInfo());
        return "user";
    }
}
