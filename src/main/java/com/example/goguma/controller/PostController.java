package com.example.goguma.controller;

import com.example.goguma.dto.PostRequestDto;
import com.example.goguma.model.Post;
import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;

@RestController
public class PostController {
    private final PostService postService;


    public PostController(PostService postService) {
        this.postService = postService;
    }


    @PostMapping(value = "/user_post", consumes = {"multipart/form-data"})
    public Post createPost(@ModelAttribute PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Post temp = postService.registerPost(postRequestDto);

        return temp;
    }

}
