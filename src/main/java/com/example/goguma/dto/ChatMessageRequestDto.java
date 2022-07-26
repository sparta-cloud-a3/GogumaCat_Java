package com.example.goguma.dto;

import com.example.goguma.model.ChatMessage;
import com.example.goguma.model.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {

    private MessageType type;
    //채팅방 ID
    private String roomId;
    //보내는 사람
    private String sender;
    //내용
    private String message;

    private String sendTime;

    public ChatMessage toEntity() {
        return new ChatMessage(type, message);
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
    }
}
