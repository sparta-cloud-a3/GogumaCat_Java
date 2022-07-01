package com.example.goguma.service;

import com.example.goguma.dto.PostImgResponseDto;
import com.example.goguma.dto.PostRequestDto;
import com.example.goguma.dto.PostResponseDto;
import com.example.goguma.model.Post;
import com.example.goguma.model.PostImg;
import com.example.goguma.model.User;
import com.example.goguma.repository.LikeRepository;
import com.example.goguma.repository.PostImgRepository;
import com.example.goguma.repository.PostRepository;
import com.example.goguma.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostTestService {
    private final PostRepository postRepository;
    private final PostImgRepository postImgRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    /**
     * 전체 게시물 가져오기
     * @return List<PostResponseDto> posts: 전체 게시물
     */
    public List<PostResponseDto> getAllPosts(String orderType) {

        List<Post> findPosts = null;
        if (orderType.equals("latest")) {
            findPosts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        } else {
            findPosts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCount"));
        }

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

    //제목, 내용으로 검색하는 부분.
    public List<PostResponseDto> getTitlePosts(String keyword) {
        List<Post> titleList = postRepository.findByTitleContainingOrContentContaining(keyword,keyword);
        List<PostResponseDto> posts = new ArrayList<>();
        List<PostImg> findPostImgs;
        PostResponseDto postResponseDto = null;
        for (Post findPost : titleList) {
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

    public PostResponseDto getOnePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 상품입니다.")
        );

        User user = post.getUser();

        PostResponseDto postResponseDto = new PostResponseDto(
                post.getId(), post.getTitle(), post.getPrice(), post.getAddress(), post.getLikeCount(),
                post.getContent(), user.getId(), user.getNickname(), user.getProfilePic(), post.getDate()
        );

        List<PostImg> findPostImgs = postImgRepository.findByPostId(postId);

        for (PostImg findPostImg : findPostImgs) {
            postResponseDto.getPostImgs().add(new PostImgResponseDto(findPostImg.getImg_url()));
        }

        return postResponseDto;
    }

    @Transactional
    public Post registerPost(PostRequestDto postRequestDto, Long userId) {
        String title = postRequestDto.getTitle();
        int price = Integer.parseInt(postRequestDto.getPrice().replace(",", ""));
        String date = postRequestDto.getDate();
        String content = postRequestDto.getContent();
        String address = postRequestDto.getAddress();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 회원 입니다.")
        );
        Post post = new Post(user, title, price, content, 0, address, date, false);

        postRepository.save(post);
        if (postRequestDto.getFile() != null) {
            postImgRepository.save(new PostImg(postRequestDto.getFile().getName(),post));
        }

        return post;
    }

    @Transactional
    public void deletePost(Long postId) {
        likeRepository.deleteByPostId(postId);
        postImgRepository.deleteAllByPostId(postId);
        postRepository.deleteById(postId);
    }

    @Transactional
    public void updatePost(Long postId, PostRequestDto postRequestDto) {
        //사진 외에 post field 변경
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다.")
        );

        post.update(postRequestDto);

        //사진 변경(사진 한 장만 가능)
        if (postRequestDto.getFile() != null) {
            postImgRepository.deleteAllByPostId(postId);
            postImgRepository.save(new PostImg(postRequestDto.getFile().getName(),post));
        }
    }
}