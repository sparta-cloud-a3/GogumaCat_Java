package com.example.goguma.controller;

import com.example.goguma.model.User;
import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/update_like")
    public String updateLike(@RequestParam String postId, @RequestParam String action, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.updateLike(Long.parseLong(postId), action, userDetails.getUsername());
        return "redirect:/post/" + postId;
    }

}
