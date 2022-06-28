package com.example.goguma.controller;

import com.example.goguma.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("id",userDetails.getId());
        model.addAttribute("nickname", userDetails.getNickname());
        model.addAttribute("username", userDetails.getUsername());
        return "index";
    }


    @GetMapping("/profileinfo/{id}")
    public String info(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("id",userDetails.getId());
        model.addAttribute("nickname", userDetails.getNickname());
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("profilePic", userDetails.getProfilePic());
        model.addAttribute("kakaoId", userDetails.getKakaoId());
        model.addAttribute("address", userDetails.getAddress());
        model.addAttribute("profileInfo", userDetails.getProfileInfo());
        return "user";
    }

    @GetMapping("/posting/{username}")
    public String posting(@PathVariable String username, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("username", userDetails.getUsername());
        return "posting";
    }
}
