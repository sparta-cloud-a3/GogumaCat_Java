package com.example.goguma.service;

import com.example.goguma.dto.PasswordCheckDto;
import com.example.goguma.dto.ProfileUpdateDto;
import com.example.goguma.model.User;
import com.example.goguma.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PwService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

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
        if (user.getProfilePic() != null){
            String[] spliturl = user.getProfilePic().split("https://gogumacat-s3.s3.ap-northeast-2.amazonaws.com/");
            s3Service.delete(spliturl[1]);
        }
        String name = s3Service.uploadToAWS(profileUpdateDto.getProfilePic());
        String profilePic = "https://gogumacat-s3.s3.ap-northeast-2.amazonaws.com/" + name;
        String password = passwordEncoder.encode(profileUpdateDto.getPassword());

        user.update(profileUpdateDto, password,profilePic);
    }
}
