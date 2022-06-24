package com.example.goguma.service;

import com.example.goguma.dto.PasswordCheckDto;
import com.example.goguma.model.User;
import com.example.goguma.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PwService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PwService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean checkPw(PasswordCheckDto passwordCheckDto){
        String nickname = passwordCheckDto.getNickname();
        String pw = passwordCheckDto.getPassword();
        Optional<User> found = userRepository.findByNickname(nickname);
        String dbPw = found.get().getPassword();
        boolean result = passwordEncoder.matches(pw, dbPw);
        return result;
    }
}
