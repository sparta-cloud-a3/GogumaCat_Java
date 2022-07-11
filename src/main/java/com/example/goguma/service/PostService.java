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

        List<Post> findPosts = null;

        if (orderType.equals("latest")) {
             findPosts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        } else {
            findPosts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCount"));
        }

        return getPostResponseDtos(findPosts);
    }

    /**
     * 제목과 내용으로 검색
     * @param keyword 검색어
     * @return 검색결과에 맞는 리스트
     */
    public List<PostResponseDto> getSearchPosts(String keyword) {
        List<Post> findPosts = postRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        return getPostResponseDtos(findPosts);
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
            uploadImg(postRequestDto, post);
        }

        return post;
    }

    @Transactional
    public void  deletePost(Long postId) {
        List<PostImg> findPostImgs = postImgRepository.findByPostId(postId);
        String[] spliturl = findPostImgs.get(0).getImg_url().split("https://gogumacat.s3.ap-northeast-2.amazonaws.com/");
        s3Service.delete(spliturl[1]);
        likeRepository.deleteByPostId(postId);
        postImgRepository.deleteAllByPostId(postId);
        postRepository.deleteById(postId);
    }

    public void deleteAllPost(Long userId) {
        List<Post> post = postRepository.findByUserId(userId);
        for(int i=0; i<post.size();i++){
            Long postId =post.get(i).getId();
            deletePost(postId);
        }
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
            uploadImg(postRequestDto, post);
        }
    }

    private List<PostResponseDto> getPostResponseDtos(List<Post> findPosts) {
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

    private void uploadImg(PostRequestDto postRequestDto, Post post) {
        String name = s3Service.uploadToAWS(postRequestDto.getFile());
        String imgUrl = "https://gogumacat.s3.ap-northeast-2.amazonaws.com/" + name;
        postImgRepository.save(new PostImg(imgUrl, post));
    }
}
