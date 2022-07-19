package com.example.goguma.controller;

import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/update_like")
    public ResponseEntity<String> updateLike(@RequestParam String postId, @RequestParam String action, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.updateLike(Long.parseLong(postId), action, userDetails.getUsername());
        String uri = "/post/" + postId;
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(uri));
        return new ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
