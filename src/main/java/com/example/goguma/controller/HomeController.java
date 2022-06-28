package com.example.goguma.controller;

import com.example.goguma.model.User;
import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class HomeController {
    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("id",userDetails.getId());
        model.addAttribute("nickname", userDetails.getNickname());
        return "index";
    }


    @GetMapping("/profileinfo/{id}")
    public String info(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("id",userDetails.getId());
        Optional<User> info = userService.profile(id);
        model.addAttribute("nickname", info.get().getNickname());
        model.addAttribute("username", info.get().getUsername());
        model.addAttribute("profilePic", info.get().getProfilePic());
        model.addAttribute("kakaoId", info.get().getKakaoId());
        model.addAttribute("address", info.get().getAddress());
        model.addAttribute("profileInfo", info.get().getProfileInfo());
        return "user";
    }
}
