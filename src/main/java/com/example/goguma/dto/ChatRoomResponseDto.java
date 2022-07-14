package com.example.goguma.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChatRoomResponseDto {
    String roomId;
    String roomName;

    public ChatRoomResponseDto(String roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }
}
