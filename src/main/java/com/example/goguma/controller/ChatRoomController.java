package com.example.goguma.controller;

import com.example.goguma.model.ChatRoom;
import com.example.goguma.model.User;
import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.ChatService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;

    @Data
    static class ChatDto {
        private String nickname;
        private String roomId;
        private Boolean isSeller;

        public ChatDto(String nickname, String roomId, Boolean isSeller) {
            this.nickname = nickname;
            this.roomId = roomId;
            this.isSeller = isSeller;
        }
    }

    @Data
    static class ChatListDto {
        private String nickname;
        private Long postId;
        private Boolean isSeller;

        public ChatListDto(String nickname, Long postId) {
            this.nickname = nickname;
            this.postId = postId;
        }
    }

    /**
     * 채팅 버튼 클린
     * if 작성자라면(판매자라면) -> 연락이 온 채팅방 리스트를 보여줌
     * if 작성자가 아니라면(소비자라면) -> 채팅을 보낼 수 있도록 채팅방을 열러줌
     * @param postId
     * @return 각자에 맞는 view를 리턴
     */
    @GetMapping("/room/enter/{postId}")
    public Object rooms(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User info = userDetails.getUser();

        ChatRoom existsRoom = chatService.isExistsRoom(info.getId(), postId);
        if (chatService.isCustomer(info, postId)) { //생성된 방이 아니라면
            if (existsRoom == null) { //만약 소비자라면 -> 새로운 방을 생성
//                model.addAttribute("nickname", info.getNickname());
//                model.addAttribute("roomId", chatService.createRoom(info, postId));
//                model.addAttribute("isSeller", false);
//                return "/roomdetail";
                return new ChatDto(info.getNickname(), chatService.createRoom(info, postId), false);
            } else { //이미 생성되어있는 방이라면 -> 생성되어있는 방을 리턴
//                model.addAttribute("nickname", info.getNickname());
//                model.addAttribute("roomId", existsRoom.getRoomId());
//                model.addAttribute("isSeller", false);
//                return "/roomdetail";
                return new ChatDto(info.getNickname(), existsRoom.getRoomId(), false);
            }
        } else {
            // 만약 판매자라면 -> 연락온 리스트를 리턴
//            model.addAttribute("nickname", info.getNickname());
//            model.addAttribute("postId", postId);
//            return "/room";
            return new ChatListDto(info.getNickname(), postId);
        }
    }

    /**
     * post에 만들어진 채팅방 리스팅
     * @param postId
     * @return post에 만들어진 채팅방
     */
    @GetMapping("/rooms/{postId}")
    @ResponseBody
    public List<ChatRoom> findRoomByPostId(@PathVariable Long postId) {
        return chatService.findRoomByPostId(postId);
    }

    /**
     * 채팅방 입장
     * @param roomId
     * @return
     * @throws IOException
     */
    @GetMapping("/mypostroom/enter/{roomId}")
    public ChatDto roomDetail(@PathVariable String roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User info = userDetails.getUser();
//        model.addAttribute("nickname", info.getNickname());
//        model.addAttribute("roomId", roomId);
//        model.addAttribute("isSeller", chatService.isSeller(userDetails.getUsername(), roomId));
//        return "/roomdetail";
        return new ChatDto(info.getNickname(), roomId, chatService.isSeller(userDetails.getUsername(), roomId));
    }

    /**
     * 특정 채팅방 조회
     * @param roomId
     * @return roomId와 같은 인덱스를 가진 채팅방 반환
     */
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatService.findById(roomId);
    }
}
