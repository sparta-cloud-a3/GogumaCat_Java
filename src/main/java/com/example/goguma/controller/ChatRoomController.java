package com.example.goguma.controller;

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
    private Map<String,Integer> map = new HashMap<String, Integer>();

    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User info = userService.profile(userDetails.getId());
        model.addAttribute("nickname", info.getNickname());
        return "/room";
    }

    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatService.findAllRoom();
    }

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        return chatService.createRoom(name);
    }

    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId, HttpServletResponse response,@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        if(map.containsKey(roomId) == false){
            map.put(roomId,1);
        }else{
            if(map.get(roomId)<2){
                map.put(roomId,2);
            }else{
                response.setContentType("text/html; charset=utf-8");
                response.getWriter().print("<script>alert('최대 2명 들어올 수 있습니다.');history.back();</script>");
                return "/room";
            }
        }
        User info = userService.profile(userDetails.getId());
        model.addAttribute("nickname", info.getNickname());
        model.addAttribute("roomId", roomId);
        return "/roomdetail";
    }

    // 채팅방 입장 화면 -userId 추가
//    @GetMapping("/room/enter/{roomId}")
//    public String roomDetail(@PathVariable String roomId, @AuthenticationPrincipal UserDetailsImpl userDetails, Model model, HttpServletResponse response) throws IOException {
//        boolean isOpened = chatService.enterRoom(roomId, userDetails.getId());
//        if(!isOpened){
//            response.setContentType("text/html; charset=utf-8");
//            response.getWriter().print("<script>alert('최대 2명 들어올 수 있습니다.');history.back();</script>");
//            return "/room";
//        }
//        model.addAttribute("roomId", roomId);
//        return "/roomdetail";
//    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatService.findById(roomId);
    }
}
