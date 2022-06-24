package com.example.goguma.repository;

import com.example.goguma.model.Post;
import com.example.goguma.model.PostImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImgRepository extends JpaRepository<PostImg, Long> {
    List<PostImg> findByPostId(Long postId);
}