package com.example.goguma.service;

import com.example.goguma.dto.ChatMessageDto;
import com.example.goguma.model.ChatMessage;
import com.example.goguma.model.ChatRoom;
import com.example.goguma.model.User;
import com.example.goguma.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final UserService userService;

    public void saveMessage(ChatMessageDto chatMessageDto){
        ChatMessage chatMessage = chatMessageDto.toEntity();
        ChatRoom room = chatService.findById(chatMessageDto.getRoomId());
        chatMessage.addRoom(room);
        User sender = userService.findByNickname(chatMessageDto.getSender());
        chatMessage.addSender(sender);

        messageRepository.save(chatMessage);
    }
}
