package com.example.goguma.controller;

import com.example.goguma.dto.PostRequestDto;
import com.example.goguma.dto.PostResponseDto;
import com.example.goguma.dto.Result;
import com.example.goguma.model.Like;
import com.example.goguma.model.Post;

import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.LikeService;
import com.example.goguma.service.PostService;
import com.example.goguma.model.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final LikeService likeService;

    @Data
    static class SimplePostDto {
        private Long userId;
        private String nickname;
        private Boolean likeByMe;
        private PostResponseDto post;

        public SimplePostDto(PostResponseDto post) {
            this.post = post;
        }

        public SimplePostDto(Long userId, String nickname, Boolean likeByMe, PostResponseDto post) {
            this.userId = userId;
            this.nickname = nickname;
            this.likeByMe = likeByMe;
            this.post = post;
        }
    }

    /**
     * 전체 게시물 리스팅
     *
     * @param orderType latest or like
     * @return 전체 게시물
     */
    @GetMapping("/post/all")
    public Result getAllPosts(@RequestParam String orderType) {
        List<PostResponseDto> posts = postService.getAllPosts(orderType);
        return new Result(posts.size(), posts);
    }

    /**
     * 회원이 게시물을 선택하면 상세페이지를 보여준다.
     *
     * @param postId
     * @return 상세 게시물
     */
    @GetMapping("/post/detail/{postId}")
    public SimplePostDto getOnePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails != null) {
            User user = userDetails.getUser();
            return new SimplePostDto(user.getId(), user.getNickname(), likeService.isLikeByMe(user.getId(), postId),postService.getOnePost(postId));
        }
        return new SimplePostDto(postService.getOnePost(postId));
    }

    @PostMapping(value = "/post/create", consumes = {"multipart/form-data"})
    public PostResponseDto createPost(@ModelAttribute PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User info = userDetails.getUser();
        Post post = postService.createPost(postRequestDto, info.getId());
        return PostResponseDto.toDto(post);
    }

    @DeleteMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl user) {
        return postService.deletePost(postId, user.getUsername());
    }

    @GetMapping("/post/update/{postId}")
    public PostResponseDto updatePostPage(@PathVariable Long postId) {
        return postService.getOnePost(postId);
    }

    @PostMapping(value = "/post/update/{postId}", consumes = {"multipart/form-data"})
    public String updatePost(@PathVariable Long postId, @ModelAttribute PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl user) {
        return postService.updatePost(postId, postRequestDto, user.getUsername());
    }

    @GetMapping("/post/search")
    public Result search(@RequestParam String query, @RequestParam String orderType) {
        List<PostResponseDto> posts = postService.getSearchPosts(query, orderType);
        return new Result(posts.size(), posts);
    }

    @GetMapping("/post/top8")
    public Result getTop8Posts() {
        List<PostResponseDto> posts = postService.getTop8Posts();
        return new Result(posts.size(), posts);
    }
}
