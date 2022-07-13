package com.example.goguma.controller;

import com.example.goguma.dto.PostRequestDto;
import com.example.goguma.dto.PostResponseDto;
import com.example.goguma.jwt.JwtProvider;
import com.example.goguma.model.Post;

import com.example.goguma.repository.LikeRepository;
import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.PostService;
import com.example.goguma.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final LikeRepository likeRepository;
    private final JwtProvider jwtProvider;

    @GetMapping("/all")
    @ResponseBody
    public List<PostResponseDto> getAllPosts(@RequestParam String orderType) {
        return postService.getAllPosts(orderType);
    }

    /**
     * 회원이 게시물을 선택하면 상세페이지를 보여준다.
     * @param postId
     * @param model // response: 게시물 정보
     * @return post.html
     */
    @RequestMapping("/post/{postId}")
    public String getOnePost(@PathVariable Long postId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User info = userDetails.getUser();
        model.addAttribute("post", postService.getOnePost(postId));
        model.addAttribute("nickname", info.getNickname());
        model.addAttribute("userId", info.getId());
        model.addAttribute("likeByMe", likeRepository.existsByuserIdAndPostId(info.getId(), postId));
        return "post";
    }


    @PostMapping(value = "/user_post", consumes = {"multipart/form-data"})
    @ResponseBody
    public Post createPost(@ModelAttribute PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User info = userDetails.getUser();
        return postService.createPost(postRequestDto, info.getId());
    }

    @DeleteMapping("post/delete/{postId}")
    public String deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return "redirect:/";
    }

    @RequestMapping("/posting_update/{postId}")
    public String updatePostPage(@PathVariable Long postId, Model model) {
        model.addAttribute("post", postService.getOnePost(postId));
        return "posting_update";
    }

    @PostMapping(value = "/post/update/{postId}", consumes = {"multipart/form-data"})
    public String updatePost(@PathVariable Long postId, @ModelAttribute PostRequestDto postRequestDto) {
        postService.updatePost(postId, postRequestDto);
        return "redirect:/post/" + postId;
    }

    //검색어(query) 를 받아서 관련 게시물 검색. query 하나로 제목, 내용을 검색한다.
    //order, page는 ajax에 있길래 일단 가져오긴 했는데 페이지랑 최신순,관심순 정렬하는 부분인가요?
    @ResponseBody
    @GetMapping("/search")
    public List<PostResponseDto> search(@RequestParam(value = "query") String query,
                                             @RequestParam(value = "order") String order, @RequestParam(value = "page") String page){

        return postService.getSearchPosts(query);
    }

}
