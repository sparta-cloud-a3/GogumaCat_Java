package com.example.goguma.controller;

import com.example.goguma.model.ChatRoom;
import com.example.goguma.model.User;
import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.ChatService;
import com.google.gson.JsonObject;
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
    @ResponseBody
    public String rooms(@PathVariable Long postId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User info = userDetails.getUser();
        JsonObject json = new JsonObject();

        ChatRoom existsRoom = chatService.isExistsRoom(info.getId(), postId);
        if (chatService.isCustomer(info, postId)) { //생성된 방이 아니라면
            if (existsRoom == null) { //만약 소비자라면 -> 새로운 방을 생성
                json.addProperty("nickname", info.getNickname());
                json.addProperty("roomId", chatService.createRoom(info, postId));
                json.addProperty("isSeller", false);
                return json.toString();
//                return "/roomdetail";
            } else { //이미 생성되어있는 방이라면 -> 생성되어있는 방을 리턴
                json.addProperty("nickname", info.getNickname());
                json.addProperty("roomId", existsRoom.getRoomId());
                json.addProperty("isSeller", false);
                return json.toString();
//                return "/roomdetail";
            }
        } else {
            // 만약 판매자라면 -> 연락온 리스트를 리턴
            json.addProperty("nickname", info.getNickname());
            json.addProperty("postId", postId);
            return json.toString();
//            return "/room";
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
    @ResponseBody
    public String roomDetail(Model model, @PathVariable String roomId, HttpServletResponse response,@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        User info = userDetails.getUser();
        JsonObject json = new JsonObject();
        json.addProperty("nickname", info.getNickname());
        json.addProperty("roomId", roomId);
        json.addProperty("isSeller", chatService.isSeller(userDetails.getUsername(), roomId));
        return json.toString();
//        return "/roomdetail";
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
