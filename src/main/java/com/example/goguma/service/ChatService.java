package com.example.goguma.service;

import com.example.goguma.model.ChatRoom;
import com.example.goguma.model.Post;
import com.example.goguma.model.User;
import com.example.goguma.repository.ChatRepository;
import com.example.goguma.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final PostRepository postRepository;

    /**
     * 소비자인지 아닌지를 확인
     * @param clickUser
     * @param postId
     * @return
     * if 소비자라면 -> true를 리턴
     * if 판매자라면 -> false를 리턴
     */
    public boolean isCustomer(User clickUser, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지않는 게시물 입니다.")
        );
        User postUser = post.getUser();

        if (clickUser.getId().equals(postUser.getId())) { //login user와 post의 user가 같다면 -> 판매자
            return false;
        }  else { //login user와 post의 user가 같다면 -> 소비자
            return true;
        }
    }

    /**
     * 방을 생성
     * @param user
     * @param postId
     * @return 생성된 방의 아이디
     */
    public String createRoom(User user, Long postId) {
        //채팅방 객체 만듦
        ChatRoom chatRoom = ChatRoom.create(user.getNickname());

        //연관관계 주입
        chatRoom.addUser(user);

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지않는 게시물 입니다.")
        );
        chatRoom.addPost(post);

        //채팅방 db에 저장
        ChatRoom save = chatRepository.save(chatRoom);
        return save.getRoomId();
    }

    /**
     * 이미 생성된 방인지 아닌지 검사
     * @param userid
     * @param postId
     * @return
     * if 생성된 방이라면 ChatRoom 객체 리턴
     * if 생성된 방이 아니라면 null 리턴
     */
    public ChatRoom isExistsRoom(Long userid, Long postId) {
        return chatRepository.findByUserIdAndPostId(userid, postId);
    }

    /**
     * 채팅방 하나 불러오기
     * @param roomId
     * @return roomId에 맞는 채팅방
     */
    public ChatRoom findById(String roomId) {
        return chatRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );
    }

    /**
     * post에 만들어진 채팅방을 리스팅
     * @param postId
     * @return postId로 만들어진 채팅방 리스트 리턴
     */
    public List<ChatRoom> findRoomByPostId(Long postId) {
        return chatRepository.findByPostId(postId);
    }

    public boolean isSeller(String username, String roomId) {
        ChatRoom chatRoom = chatRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );
        String seller = chatRoom.getPost().getUser().getUsername();
        return seller.equals(username);
    }
}
