package com.example.goguma.controller;

import com.example.goguma.dto.PostResponseDto;
import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/all")
    @ResponseBody
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllPosts();
    }

    /**
     * 회원이 게시물을 선택하면 상세페이지를 보여준다.
     * @param postId
     * @param model // response: 게시물 정보
     * @param userDetails // 로그인 계정 정보
     * @return post.html
     */
    @GetMapping("/post/{postId}")
    public String getOnePost(@PathVariable Long postId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("post", postService.getOnePost(postId));
        model.addAttribute("nickname", userDetails.getNickname());
        model.addAttribute("userId", userDetails.getId());
        return "post";
    }

    //걍 테스트 지워야 됨
    @GetMapping("/user/{postId}/test")
    @ResponseBody
    public PostResponseDto getOnePostTest(@PathVariable Long postId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getOnePost(postId);
    }


}
