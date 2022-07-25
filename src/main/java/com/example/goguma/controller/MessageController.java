package com.example.goguma.controller;

import com.example.goguma.dto.ChatMessageDto;
import com.example.goguma.model.MessageType;
import com.example.goguma.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final MessageService messageService;

    @MessageMapping("/chat/message")
    public void enter(@RequestBody ChatMessageDto message) {
        if (message.getType().equals(MessageType.ENTER)) {
            message.setMessage(message.getSender()+"님이 입장하였습니다.");
        }
        //메세지 저장

        sendingOperations.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }
}
