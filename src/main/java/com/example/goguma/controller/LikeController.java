package com.example.goguma.controller;

import com.example.goguma.jwt.JwtProvider;
import com.example.goguma.model.User;
import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final JwtProvider jwtProvider;

    @PostMapping("/update_like")
    public String updateLike(@RequestParam String postId, @RequestParam String action, @CookieValue(name = "mytoken") String token) {
        User info = jwtProvider.getUser(token);
        likeService.updateLike(Long.parseLong(postId), action, info);
        return "redirect:/post/" + postId;
    }

}
