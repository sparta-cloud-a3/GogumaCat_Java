package com.example.goguma.repository;

import com.example.goguma.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByuserIdAndPostId(Long userId, Long PostId);

    void deleteByUserIdAndPostId(Long userId, Long postId);

    void deleteByPostId(Long postId);
    void deleteByUserId(Long UserId);

    List<Like> findByUserId(Long userId);
}
