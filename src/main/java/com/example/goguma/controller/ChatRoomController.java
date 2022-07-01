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
//    @GetMapping("/room/enter/{postId}")
//    public String rooms(@PathVariable Long postId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        User info = userService.profile(userDetails.getId());
//        model.addAttribute("nickname", info.getNickname());
//        System.out.println("postId = " + postId);
//        return "/room";
//    }

    /**
     * 채팅 버튼 클린
     * if 작성자라면(판매자라면) -> 연락이 온 채팅방 리스트를 보여줌
     * if 작성자가 아니라면(소비자라면) -> 채팅을 보낼 수 있도록 채팅방을 열러줌
     * @param postId
     * @param model
     * @param userDetails
     * @return 각자에 맞는 view를 리턴
     */
    @GetMapping("/room/enter/{postId}")
    public String rooms(@PathVariable Long postId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User info = userService.profile(userDetails.getId());
        ChatRoom existsRoom = chatService.isExistsRoom(info.getId(), postId);
        if ( existsRoom == null) { //생성된 방이 아니라면
            if (chatService.isCustomer(info, postId)) { //만약 소비자라면 -> 새로운 방을 생성
                model.addAttribute("nickname", info.getNickname());
                model.addAttribute("roomId", chatService.createRoom(info, postId));
                return "/roomdetail";
            } else { // 만약 판매자라면 -> 연락온 리스트를 리턴
                model.addAttribute("nickname", info.getNickname());
                model.addAttribute("postId", postId);
                return "/room";
            }
        } else {//이미 생성되어있는 방이라면 -> 생성되어있는 방을 리턴
            model.addAttribute("nickname", info.getNickname());
            model.addAttribute("roomId", existsRoom.getRoomId());
            return "/roomdetail";
        }
    }

    // 모든 채팅방 목록 반환
//    @GetMapping("/rooms")
//    @ResponseBody
//    public List<ChatRoom> room() {
//        return chatService.findAllRoom();
//    }

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

//    // 채팅방 생성
//    @PostMapping("/room")
//    @ResponseBody
//    public ChatRoom createRoom(@RequestParam String name) {
//        return chatService.createRoom(name);
//    }

    /**
     * 채팅방 입장
     * @param model
     * @param roomId
     * @param response
     * @param userDetails
     * @return
     * @throws IOException
     */
    @GetMapping("/mypostroom/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId, HttpServletResponse response,@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
//        if(map.containsKey(roomId) == false){ //postId로 필터링된 리스트에서 판매자가 선택하는 구조니깐 어차피 두명만 가능
//            map.put(roomId,1);
//        }else{
//            if(map.get(roomId)<2){
//                map.put(roomId,2);
//            }else{
//                response.setContentType("text/html; charset=utf-8");
//                response.getWriter().print("<script>alert('최대 2명 들어올 수 있습니다.');history.back();</script>");
//                return "/room";
//            }
//        }
        User info = userService.profile(userDetails.getId());
        model.addAttribute("nickname", info.getNickname());
        model.addAttribute("roomId", roomId);
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
