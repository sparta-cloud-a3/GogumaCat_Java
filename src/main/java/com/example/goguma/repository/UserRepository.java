package com.example.goguma.repository;

import com.example.goguma.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByKakaoId(Long kakaoId);
}
