package com.example.goguma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPostDto {
    private Long postId;
    private String title;
    private String sellerNickname;
}
