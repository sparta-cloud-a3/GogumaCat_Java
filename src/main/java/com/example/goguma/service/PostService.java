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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostImgRepository postImgRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final S3Service s3Service;

    /**
     * 전체 게시물 가져오기
     * @return List<PostResponseDto> posts: 전체 게시물
     */
    public List<PostResponseDto> getAllPosts(String orderType) {
        List<PostResponseDto> posts;

        if (orderType.equals("latest")) { //최신순
             posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream().map(
                     p -> {
                         return new PostResponseDto(
                                 p.getId(), p.getTitle(), p.getPrice(), p.getAddress(), p.getLikeCount()
                         );
                     }
             ).collect(Collectors.toList());
        } else { //인기순
            posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCount")).stream().map(
                    p -> {
                        return new PostResponseDto(
                                p.getId(), p.getTitle(), p.getPrice(), p.getAddress(), p.getLikeCount()
                        );
                    }
            ).collect(Collectors.toList());;
        }

        for (PostResponseDto post : posts) { //fetch type이 LAZY이기 때문에 하나씩 받아오기
            postImgRepository.findByPostId(post.getPostId()).stream().forEach(
                    pi -> {
                        post.getPostImgs().add(new PostImgResponseDto(pi.getImg_url()));
                    }
            );
        }

        return posts;
    }

    /**
     * 제목과 내용으로 검색
     * @param keyword 검색어
     * @return 검색결과에 맞는 리스트
     */
    public List<PostResponseDto> getSearchPosts(String keyword) {
        List<PostResponseDto> posts = postRepository.findByTitleContainingOrContentContaining(keyword, keyword).stream().map(
                p -> {
                    return new PostResponseDto(
                            p.getId(), p.getTitle(), p.getPrice(), p.getAddress(), p.getLikeCount()
                    );
                }
        ).collect(Collectors.toList());

        for (PostResponseDto post : posts) { //fetch type이 LAZY이기 때문에 하나씩 받아오기
            postImgRepository.findByPostId(post.getPostId()).stream().forEach(
                    pi -> {
                        post.getPostImgs().add(new PostImgResponseDto(pi.getImg_url()));
                    }
            );
        }

        return posts;
    }

    public PostResponseDto getOnePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 상품입니다.")
        );
        PostResponseDto postResponseDto = PostResponseDto.toDto(post);

        postImgRepository.findByPostId(postId).stream().forEach(
                pi -> {
                    postResponseDto.getPostImgs().add(new PostImgResponseDto(pi.getImg_url()));
                }
        );

        return postResponseDto;
    }

    @Transactional
    public Post createPost(PostRequestDto postRequestDto, Long userId) {
        Post post = postRequestDto.toEntity();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 회원 입니다.")
        );
        user.addPost(post);
        postRepository.save(post);
        if (postRequestDto.getFile() != null) {
            String name = s3Service.uploadToAWS(postRequestDto.getFile());
            String imgUrl = "https://gogumacat.s3.ap-northeast-2.amazonaws.com/" + name;
            PostImg postImg = new PostImg(imgUrl);

            post.addPostImg(postImg);
        }

        return post;
    }

    @Transactional
    public void  deletePost(Long postId) {
        List<PostImg> findPostImgs = postImgRepository.findByPostId(postId);
        String[] spliturl = findPostImgs.get(0).getImg_url().split("https://gogumacat.s3.ap-northeast-2.amazonaws.com/");
        s3Service.delete(spliturl[1]);
        likeRepository.deleteByPostId(postId);
        postRepository.deleteById(postId);
    }

    @Transactional
    public void updatePost(Long postId, PostRequestDto postRequestDto) {
        //사진 외에 post field 변경
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다.")
        );

        post.update(postRequestDto);

        List<PostImg> findPostImgs = postImgRepository.findByPostId(postId);

        //사진 변경(사진 한 장만 가능)
        if (postRequestDto.getFile() != null) {
            postImgRepository.deleteAllByPostId(postId);
            String[] spliturl = findPostImgs.get(0).getImg_url().split("https://gogumacat.s3.ap-northeast-2.amazonaws.com/");
            s3Service.delete(spliturl[1]);

            String name = s3Service.uploadToAWS(postRequestDto.getFile());
            String imgUrl = "https://gogumacat.s3.ap-northeast-2.amazonaws.com/" + name;
            PostImg postImg = new PostImg(imgUrl);
            post.addPostImg(postImg);
        }
    }
}
