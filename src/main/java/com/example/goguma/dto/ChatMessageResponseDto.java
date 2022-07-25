package com.example.goguma.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ChatMessageResponseDto {
    private String sender;
    private String message;
    private LocalDateTime sendTime;

    public ChatMessageResponseDto(String sender, String message, LocalDateTime sendTime) {
        this.sender = sender;
        this.message = message;
        this.sendTime = sendTime;
    }
}
