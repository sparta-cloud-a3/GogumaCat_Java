package com.example.goguma.repository;

import com.example.goguma.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    List<Post> findByTitleContainingOrContentContaining(String title, String content);

    void deleteByUserId(Long userId);
}
