package com.example.goguma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
@AllArgsConstructor
public class TokenDto {
    private String accessToken;
    private Long refreshId;
}
