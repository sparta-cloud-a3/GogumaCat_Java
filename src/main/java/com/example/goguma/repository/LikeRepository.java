package com.example.goguma.repository;

import com.example.goguma.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByuserIdAndPostId(Long userId, Long PostId);

    void deleteByUserIdAndPostId(Long userId, Long postId);
}
