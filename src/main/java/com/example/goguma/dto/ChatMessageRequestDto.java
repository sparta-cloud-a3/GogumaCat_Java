package com.example.goguma.dto;

import com.example.goguma.model.ChatMessage;
import com.example.goguma.model.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public ChatMessage toEntity() {
        return new ChatMessage(type, message);
    }
}
