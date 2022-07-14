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
    public boolean password(@RequestBody PasswordCheckDto passwordCheckDto){
        return pwService.checkPw(passwordCheckDto);
    }
    @PutMapping(value = "/update_profile/{id}", consumes = {"multipart/form-data"})
    public Long updateProfile(@PathVariable Long id, ProfileUpdateDto profileUpdateDto){
        pwService.update(id, profileUpdateDto);
        return id;
    }

}
