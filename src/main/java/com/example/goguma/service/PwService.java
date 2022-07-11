package com.example.goguma.service;

import com.example.goguma.dto.PasswordCheckDto;
import com.example.goguma.dto.ProfileUpdateDto;
import com.example.goguma.model.User;
import com.example.goguma.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Transactional
    public void update(Long id, ProfileUpdateDto profileUpdateDto){
        User user = userRepository.findById(id).orElseThrow(
                () -> new NullPointerException("ID가 존재하지 않습니다.")
        );
        String password = passwordEncoder.encode(profileUpdateDto.getPassword());

        user.update(profileUpdateDto,password);
    }
}
