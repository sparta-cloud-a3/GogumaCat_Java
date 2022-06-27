package com.example.goguma.dto;

import com.example.goguma.model.PostImg;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private String content;
    private List<PostImgResponseDto> postImgs = new ArrayList<>();

    private Long writeUserId;
    private String writerNickname;

    private String date;

    public PostResponseDto(Long postId, String title, int price, String address, int likeCount) {
        this.postId = postId;
        this.title = title;
        this.price = price;
        this.address = address;
        this.likeCount = likeCount;
    }

    public PostResponseDto(Long postId, String title, int price, String address, int likeCount, String content, Long writeUserId, String writerNickname, LocalDateTime date) {
        this.postId = postId;
        this.title = title;
        this.price = price;
        this.address = address;
        this.likeCount = likeCount;
        this.content = content;
        this.writeUserId = writeUserId;
        this.writerNickname = writerNickname;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
