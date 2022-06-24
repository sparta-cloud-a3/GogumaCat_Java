package com.example.goguma.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostImgResponseDto {
    private String imgUrl;

    public PostImgResponseDto(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
