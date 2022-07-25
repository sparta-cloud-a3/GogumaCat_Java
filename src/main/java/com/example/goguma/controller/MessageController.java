package com.example.goguma.controller;

import com.example.goguma.dto.ChatMessageRequestDto;
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
    public void enter(@RequestBody ChatMessageRequestDto message) {
        System.out.println("MessageController.enter");
        if (message.getType().equals(MessageType.ENTER)) {
                message.setMessage(message.getSender()+"님이 입장하였습니다.");
        } else {
            messageService.saveMessage(message);
        }

        sendingOperations.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }
}
