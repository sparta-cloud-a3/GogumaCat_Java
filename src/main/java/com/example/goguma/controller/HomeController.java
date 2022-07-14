package com.example.goguma.controller;

import com.example.goguma.model.User;
import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.UserService;
import com.google.gson.JsonObject;
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
    public String info(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User tokenInfo = userDetails.getUser();
        User info = userService.profile(id);

        JsonObject json = new JsonObject();
        json.addProperty("id",tokenInfo.getId());
        json.addProperty("pageUserId",info.getId());
        json.addProperty("nickname", info.getNickname());
        json.addProperty("username", info.getUsername());
        json.addProperty("profilePic", info.getProfilePic());
        json.addProperty("address", info.getAddress());
        json.addProperty("profileInfo", info.getProfileInfo());
        json.addProperty("kakaoId", info.getKakaoId());

        return json.toString();
    }

    @GetMapping("/posting/{username}")
    public String posting(@PathVariable String username, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User info = userDetails.getUser();
        JsonObject json = new JsonObject();
        json.addProperty("username", info.getUsername());
        return json.toString();
    }
}
