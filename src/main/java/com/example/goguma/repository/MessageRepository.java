package com.example.goguma.repository;

import com.example.goguma.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {

}