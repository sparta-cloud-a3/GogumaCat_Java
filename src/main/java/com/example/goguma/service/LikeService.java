package com.example.goguma.service;

import com.example.goguma.exception.NoSuchPostException;
import com.example.goguma.exception.NoSuchUserException;
import com.example.goguma.model.Like;
import com.example.goguma.model.Post;
import com.example.goguma.model.User;
import com.example.goguma.repository.LikeRepository;
import com.example.goguma.repository.PostRepository;
import com.example.goguma.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Long updateLike(Long postId, String action, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                NoSuchUserException::new
        );
        Post post = postRepository.findById(postId).orElseThrow(
                NoSuchPostException::new
        );

        if (action.equals("like")) {
            Like like = new Like(post, user);
            user.addLike(like);
            likeRepository.save(like);
        } else {
            likeRepository.deleteByUserIdAndPostId(user.getId(), postId);
        }

        updateLikeCount(post, action);
        return postId;
    }

    public void updateLikeCount(Post post, String action){
        post.updateLikeCount(action);
    }
}
