package com.example.goguma.service;

import com.example.goguma.dto.PasswordCheckDto;
import com.example.goguma.dto.ProfileUpdateDto;
import com.example.goguma.exception.NoSuchUserException;
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
        User found = userRepository.findByNickname(nickname).orElseThrow(
                NoSuchUserException::new
        );
        String dbPw = found.getPassword();
        return passwordEncoder.matches(pw, dbPw);
    }

    @Transactional
    public void update(Long id, ProfileUpdateDto profileUpdateDto) {
        User user = userRepository.findById(id).orElseThrow(
                NoSuchUserException::new
        );
        if (profileUpdateDto.getProfilePic() != null){
            if (user.getProfilePic() == null){
                String name = s3Service.uploadToAWS(profileUpdateDto.getProfilePic());
                String profilePic = "https://gogumacat-s3.s3.ap-northeast-2.amazonaws.com/" + name;
                String password = passwordEncoder.encode(profileUpdateDto.getPassword());
                user.update(profileUpdateDto, password,profilePic);
            }
            if(user.getProfilePic().contains("kakaocdn")){

                String name = s3Service.uploadToAWS(profileUpdateDto.getProfilePic());
                String profilePic = "https://gogumacat-s3.s3.ap-northeast-2.amazonaws.com/" + name;
                String password = passwordEncoder.encode(profileUpdateDto.getPassword());
                user.update(profileUpdateDto, password,profilePic);
            }

            if(user.getProfilePic() != null){
                String[] spliturl = user.getProfilePic().split("https://gogumacat-s3.s3.ap-northeast-2.amazonaws.com/");
                s3Service.delete(spliturl[1]);

                String name = s3Service.uploadToAWS(profileUpdateDto.getProfilePic());
                String profilePic = "https://gogumacat-s3.s3.ap-northeast-2.amazonaws.com/" + name;
                String password = passwordEncoder.encode(profileUpdateDto.getPassword());
                user.update(profileUpdateDto, password,profilePic);

            }
        } else {
            String profilePic = user.getProfilePic();
            String password = passwordEncoder.encode(profileUpdateDto.getPassword());
            user.update(profileUpdateDto, password,profilePic);
        }

    }
}
