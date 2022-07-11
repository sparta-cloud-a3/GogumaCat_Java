package com.example.goguma.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
@Getter
@NoArgsConstructor
public class UserRequestDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginDto {
        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    @Getter
    public static class RefreshTokenDto {
        private Long refreshId;
    }
}
