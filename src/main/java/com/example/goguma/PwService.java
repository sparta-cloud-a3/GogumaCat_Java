package com.example.goguma;

import com.example.goguma.login.User;
import com.example.goguma.login.repository.UserRepository;
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

    public int checkPw(PasswordCheckDto passwordCheckDto){
        String pw = passwordCheckDto.getPassword();
//        String encoderPw = passwordEncoder.encode(pw);
        Optional<User> found = userRepository.findByPassword(pw);

        int count = 0;
        if (found.isPresent()) {
            count += 1;
        } else {
            count = 0;
        }
        return count;
    }
}
