package com.example.goguma.service;

import com.example.goguma.dto.TokenDto;
import com.example.goguma.dto.UserRequestDto;
import com.example.goguma.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public String login(UserRequestDto.LoginDto loginDto) throws IllegalArgumentException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("요청받은 아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return jwtProvider.createAccessToken(loginDto);
    }
}
