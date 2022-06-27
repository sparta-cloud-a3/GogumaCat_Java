package com.example.goguma.dto;

import com.example.goguma.model.PostImg;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long postId;
    private String title;
    private int price;
    private String address;
    private int likeCount;
    private List<PostImgResponseDto> postImgs = new ArrayList<>();

    public PostResponseDto(Long postId, String title, int price, String address, int likeCount) {
        this.postId = postId;
        this.title = title;
        this.price = price;
        this.address = address;
        this.likeCount = likeCount;
    }
}
