package com.example.goguma.controller;

import com.example.goguma.jwt.JwtProvider;
import com.example.goguma.model.ChatRoom;
import com.example.goguma.model.User;
import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.ChatService;
import com.example.goguma.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private Map<String,Integer> map = new HashMap<String, Integer>();

    /**
     * 채팅 버튼 클린
     * if 작성자라면(판매자라면) -> 연락이 온 채팅방 리스트를 보여줌
     * if 작성자가 아니라면(소비자라면) -> 채팅을 보낼 수 있도록 채팅방을 열러줌
     * @param postId
     * @param model
     * @return 각자에 맞는 view를 리턴
     */
    @GetMapping("/room/enter/{postId}")
    public String rooms(@PathVariable Long postId, Model model, @CookieValue(name = "mytoken") String token) {
        User info = jwtProvider.getUser(token);

        ChatRoom existsRoom = chatService.isExistsRoom(info.getId(), postId);
        if (chatService.isCustomer(info, postId)) { //생성된 방이 아니라면
            if (existsRoom == null) { //만약 소비자라면 -> 새로운 방을 생성
                model.addAttribute("nickname", info.getNickname());
                model.addAttribute("roomId", chatService.createRoom(info, postId));
                model.addAttribute("isSeller", false);
                return "/roomdetail";
            } else { //이미 생성되어있는 방이라면 -> 생성되어있는 방을 리턴
                model.addAttribute("nickname", info.getNickname());
                model.addAttribute("roomId", existsRoom.getRoomId());
                model.addAttribute("isSeller", false);
                return "/roomdetail";
            }
        } else {
            // 만약 판매자라면 -> 연락온 리스트를 리턴
            model.addAttribute("nickname", info.getNickname());
            model.addAttribute("postId", postId);
            return "/room";
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
     * @param model
     * @param roomId
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/mypostroom/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId, HttpServletResponse response,@CookieValue(name = "mytoken") String token) throws IOException {
        User info = jwtProvider.getUser(token);
        model.addAttribute("nickname", info.getNickname());
        model.addAttribute("roomId", roomId);
        model.addAttribute("isSeller", chatService.isSeller(info, roomId));
        return "/roomdetail";
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
