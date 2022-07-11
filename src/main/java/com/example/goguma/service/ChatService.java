package com.example.goguma.service;

import com.example.goguma.model.ChatRoom;
import com.example.goguma.model.Post;
import com.example.goguma.model.User;
import com.example.goguma.repository.ChatRepository;
import com.example.goguma.repository.PostRepository;
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
    @Transactional
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

    public boolean isSeller(User user, String roomId) {
        ChatRoom chatRoom = chatRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );

        return chatRoom.getPost().getUser().equals(user);
    }

//    //채팅방 전체 불러오기
//    public List<ChatRoom> findAllRoom() {
//        return chatRepository.findAll(Sort.by(Sort.Direction.DESC, "roomId"));
//    }
//
//    //채팅방 생성
//    @Transactional
//    public ChatRoom createRoom(String name) {
//        //post도 설정해줘야함
//        ChatRoom chatRoom = chatRepository.save(ChatRoom.create(name));
//        return chatRoom;
//    }
//
//    //post에 따라 채팅방 불러오기
//    public List<ChatRoom> findAllRoomByPostId(Long postId) {
//        return chatRepository.findByPostId(postId);
//    }
//
//    @Transactional
//    public boolean enterRoom(String roomId, Long userId) {
//        ChatRoom chatRoom = chatRepository.findById(roomId).orElseThrow(
//                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
//        );
//
//        User user = chatRoom.getUser();
//        //만약 post 직상자의 userId가 같다면 true를 리턴하고 끝내야 됨 -> 판매자이므로 연결된 유저로 카운트하면 안된다.
//
//        if (user != null) { //사용자가 이미 존재한다면 채팅방 가득 찬 것이기에 false 리턴
//            return false;
//        } else { //사용자가 없다면 사용자를 등록하고 true 리턴하며 채팅 시작
//            user = userRepository.findById(userId).orElseThrow(
//                    () -> new IllegalArgumentException("존재하지않는 사용자 입니다.")
//            );
//            chatRoom.addUser(user);
//        }
//        return true;
//    }
}
