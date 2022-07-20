package com.example.goguma.service;

import com.example.goguma.dto.UserRequestDto;
import com.example.goguma.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional
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
            log.error(e.getMessage());
        }
        jwtProvider.createRefreshToken(loginDto);

        return jwtProvider.createAccessToken(loginDto);
    }
}
