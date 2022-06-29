package com.example.goguma.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateDto {
    private String username;
    private String nickname;
    private String password;
    private String address;
    private String profilePic;
    private String profileInfo;
}
