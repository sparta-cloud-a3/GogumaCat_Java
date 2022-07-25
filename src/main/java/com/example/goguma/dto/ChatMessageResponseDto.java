package com.example.goguma.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
public class ChatMessageResponseDto {
    private String sender;
    private String message;
    private String sendTime;

    public ChatMessageResponseDto(String sender, String message, LocalDateTime sendTime) {
        this.sender = sender;
        this.message = message;
        this.sendTime = sendTime.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );;
    }
}
