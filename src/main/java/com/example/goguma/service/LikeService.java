package com.example.goguma.service;

import com.example.goguma.model.Like;
import com.example.goguma.model.Post;
import com.example.goguma.model.User;
import com.example.goguma.repository.LikeRepository;
import com.example.goguma.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Transactional
    public void updateLike(Long postId, String action, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다.")
        );
        if (action.equals("like")) {
            likeRepository.save(new Like(post, user));
        } else {
            likeRepository.deleteByUserIdAndPostId(user.getId(), postId);
        }
        updateLikeCount(post, action);
    }

    @Transactional
    public void updateLikeCount(Post post, String action){
        post.updateLikeCount(action);
    }

    @Transactional
    public void deleteLike(Long userId){
        likeRepository.deleteByUserId(userId);
    }
}
