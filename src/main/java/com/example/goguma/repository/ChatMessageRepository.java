package com.example.goguma.repository;

import com.example.goguma.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
//    boolean existsByRoomIdAndUserId(String roomId, Long userId);
}