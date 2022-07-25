package com.example.goguma.service;

import com.example.goguma.dto.ChatMessageRequestDto;
import com.example.goguma.model.ChatMessage;
import com.example.goguma.model.ChatRoom;
import com.example.goguma.model.User;
import com.example.goguma.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    private final ChatMessageRepository messageRepository;
    private final ChatService chatService;
    private final UserService userService;

    public void saveMessage(ChatMessageRequestDto chatMessageDto){
        ChatMessage chatMessage = chatMessageDto.toEntity();

        ChatRoom room = chatService.findById(chatMessageDto.getRoomId());
        User sender = userService.findByNickname(chatMessageDto.getSender());
        chatMessage.addRoom(room);
        chatMessage.addSender(sender);

        messageRepository.save(chatMessage);
    }
}
