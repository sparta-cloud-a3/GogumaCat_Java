package com.example.goguma.controller;

import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/update_like")
    public Long updateLike(@RequestParam String postId, @RequestParam String action, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.updateLike(Long.parseLong(postId), action, userDetails.getUsername());
    }
}
