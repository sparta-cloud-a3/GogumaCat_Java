package com.example.goguma.service;

import com.example.goguma.model.ChatRoom;
import com.example.goguma.model.User;
import com.example.goguma.repository.ChatRepository;
import com.example.goguma.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    //채팅방 전체 불러오기
    public List<ChatRoom> findAllRoom() {
        return chatRepository.findAll(Sort.by(Sort.Direction.DESC, "roomId"));
    }

    //채팅방 하나 불러오기
    public ChatRoom findById(String roomId) {
        return chatRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );
    }

    //채팅방 생성
    @Transactional
    public ChatRoom createRoom(String name) {
        //post도 설정해줘야함
        ChatRoom chatRoom = chatRepository.save(ChatRoom.create(name));
        return chatRoom;
    }

    //post에 따라 채팅방 불러오기
    public List<ChatRoom> findAllRoomByPostId(Long postId) {
        return chatRepository.findByPostId(postId);
    }

    @Transactional
    public boolean enterRoom(String roomId, Long userId) {
        ChatRoom chatRoom = chatRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );

        User user = chatRoom.getUser();
        //만약 post 직상자의 userId가 같다면 true를 리턴하고 끝내야 됨 -> 판매자이므로 연결된 유저로 카운트하면 안된다.

        if (user != null) { //사용자가 이미 존재한다면 채팅방 가득 찬 것이기에 false 리턴
            return false;
        } else { //사용자가 없다면 사용자를 등록하고 true 리턴하며 채팅 시작
            user = userRepository.findById(userId).orElseThrow(
                    () -> new IllegalArgumentException("존재하지않는 사용자 입니다.")
            );
            chatRoom.addUser(user);
        }

        return true;
    }
}
