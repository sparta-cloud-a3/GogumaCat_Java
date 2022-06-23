package com.example.goguma.login.repository;

import com.example.goguma.login.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByPassword(String password);

}
