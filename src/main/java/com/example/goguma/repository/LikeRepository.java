package com.example.goguma.repository;

import com.example.goguma.model.Like;
import com.example.goguma.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByuserIdAndPostId(Long userId, Long PostId);

    void deleteByUserIdAndPostId(Long userId, Long postId);

    void deleteByPostId(Long postId);

    List<Like> findByUserId(Long userId);
}
