package com.example.goguma.controller;

import com.example.goguma.dto.PasswordCheckDto;
import com.example.goguma.dto.ProfileUpdateDto;
import com.example.goguma.service.PwService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileinfoController {

    private final PwService pwService;

    @PostMapping("/profileinfo/check")
    public boolean password(PasswordCheckDto passwordCheckDto){
        return pwService.checkPw(passwordCheckDto);
    }
    @PutMapping("/update_profile/{id}")
    public Long updateProfile(@PathVariable Long id, @RequestBody ProfileUpdateDto profileUpdateDto){
        pwService.update(id, profileUpdateDto);
        return id;
    }

}
