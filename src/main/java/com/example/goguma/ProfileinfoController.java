package com.example.goguma;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileinfoController {

    private final PwService pwService;

    public ProfileinfoController(PwService pwService) {
        this.pwService = pwService;
    }

    @PostMapping("/profileinfo/check")
    public int password(PasswordCheckDto passwordCheckDto){
        int result = pwService.checkPw(passwordCheckDto);
        return result;
        // 비밀번호가 encode되어서 그냥 입력하면 찾지 못함. 따로 encoder를 돌려도 안됨...
        // DB에서 암호화되서 저장된 비밀번호를 붙여넣으면 정상작동.
        // 남의 비밀번호를 찍어도 수정창이 뜸;
    }

}
