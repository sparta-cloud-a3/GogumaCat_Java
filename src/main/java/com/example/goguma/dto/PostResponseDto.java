package com.example.goguma.dto;

import com.example.goguma.model.PostImg;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long post_id;
    private String title;
    private int price;
    private String address;
    private int likeCount;
    private List<PostImgResponseDto> postImgs;

    public PostResponseDto(Long post_id, String title, int price, String address, int likeCount, List<PostImgResponseDto> postImgs) {
        this.post_id = post_id;
        this.title = title;
        this.price = price;
        this.address = address;
        this.likeCount = likeCount;
        this.postImgs = postImgs;
    }
}
