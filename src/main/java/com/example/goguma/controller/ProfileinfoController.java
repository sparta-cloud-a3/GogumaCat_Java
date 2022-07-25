package com.example.goguma.controller;

import com.example.goguma.dto.PasswordCheckDto;
import com.example.goguma.dto.ProfileUpdateDto;
import com.example.goguma.security.UserDetailsImpl;
import com.example.goguma.service.PwService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileinfoController {

    private final PwService pwService;

    @PostMapping("/profileinfo/check")
    public boolean password(@RequestBody PasswordCheckDto passwordCheckDto){
        return pwService.checkPw(passwordCheckDto);
    }
    @PutMapping(value = "/update_profile/{id}", consumes = {"multipart/form-data"})
    public String updateProfile(@PathVariable Long id, ProfileUpdateDto profileUpdateDto, @AuthenticationPrincipal UserDetailsImpl user){
        if(!id.equals(user.getUser().getId())) {
            return "회원정보 수정은 본인만 가능합니다.";
        }
        pwService.update(id, profileUpdateDto);
        return "회원정보 수정 성공하였습니다.";
    }

}
