package com.example.goguma.controller;

import com.example.goguma.model.User;
import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.UserService;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    @RequestMapping("/")
    public String home(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User info = userDetails.getUser();
        JsonObject json = new JsonObject();
        json.addProperty("id",info.getId());
        json.addProperty("nickname", info.getNickname());
        return json.toString();
    }

    @GetMapping("/profileinfo/{id}")
    public simpleUserDto info(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User tokenInfo = userDetails.getUser();
        User info = userService.profile(id);

        return new simpleUserDto(
                tokenInfo.getId(),
                info.getId(),
                info.getKakaoId(),
                info.getUsername(),
                info.getNickname(),
                info.getProfilePic(),
                info.getProfileInfo(),
                info.getAddress());
    }
    @Data
    static class simpleUserDto{
        private Long id;
        private Long pageUserId;
        private Long kakaoId;
        private String username;
        private String nickname;
        private String profilePic;
        private String profileInfo;
        private String address;
        public simpleUserDto(Long id, Long pageUserId, Long kakaoId, String username, String nickname, String profilePic, String profileInfo, String address) {
            this.id = id;
            this.pageUserId = pageUserId;
            this.kakaoId = kakaoId;
            this.username = username;
            this.nickname = nickname;
            this.profilePic = profilePic;
            this.profileInfo = profileInfo;
            this.address = address;
        }
    }

    @GetMapping("/posting/{username}")
    public String posting(@PathVariable String username, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User info = userDetails.getUser();
        JsonObject json = new JsonObject();
        json.addProperty("username", info.getUsername());
        return json.toString();
    }
}
