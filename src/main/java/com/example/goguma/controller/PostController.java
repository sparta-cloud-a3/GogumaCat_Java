package com.example.goguma.controller;

import com.example.goguma.dto.PostResponseDto;
import com.example.goguma.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/all")
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllPosts();
    }
}
