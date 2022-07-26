package com.example.goguma.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProfileUpdateDto {
    private String username;
    private String nickname;
    private String address;
    private MultipartFile profilePic;
    private String profileInfo;

    @Override
    public String toString() {
        return "ProfileUpdateDto{" +
                "username=" + username + '\'' +
                ", nickname=" + nickname + '\'' +
                ", address=" + address + '\'' +
                ", profilePic="+profilePic+ '\'' +
                ", profileInfo="+profileInfo+ '\'' +
                '}';
    }
}
