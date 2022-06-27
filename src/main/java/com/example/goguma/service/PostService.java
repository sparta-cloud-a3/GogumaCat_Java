package com.example.goguma.service;

import com.example.goguma.dto.PostImgResponseDto;
import com.example.goguma.dto.PostResponseDto;
import com.example.goguma.model.Post;
import com.example.goguma.model.PostImg;
import com.example.goguma.repository.PostImgRepository;
import com.example.goguma.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostImgRepository postImgRepository;

    /**
     * 전체 게시물 가져오기
     * @return List<PostResponseDto> posts: 전체 게시물
     */
    public List<PostResponseDto> getAllPosts() {
        List<Post> findPosts = postRepository.findAll();
        List<PostResponseDto> posts = new ArrayList<>();
        List<PostImg> findPostImgs;
        PostResponseDto postResponseDto = null;
        for (Post findPost : findPosts) {
             postResponseDto = new PostResponseDto(
                    findPost.getId(), findPost.getTitle(), findPost.getPrice(), findPost.getAddress(), findPost.getLikeCount()
            );
            findPostImgs = postImgRepository.findByPostId(findPost.getId());
            //post의 사진 추가
            for (PostImg findPostImg : findPostImgs) {
                postResponseDto.getPostImgs().add(new PostImgResponseDto(findPostImg.getImg_url()));
            }
            posts.add(postResponseDto);
        }

        return posts;
    }
}
