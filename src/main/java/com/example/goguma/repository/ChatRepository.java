package com.example.goguma.repository;

import com.example.goguma.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatRoom, String> {
    List<ChatRoom> findByPostId(Long postId);
}
