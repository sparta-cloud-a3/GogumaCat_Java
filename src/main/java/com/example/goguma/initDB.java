package com.example.goguma;

import com.example.goguma.model.Post;
import com.example.goguma.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class initDB {
    private final EntityManager em;

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;

        public void dbInit1() {
            String pw = passwordEncoder.encode("1234");
            User user1 = new User("user1", pw, "nickname1", "서울시 중구 뭐동");
            User user2 = new User("user2", pw, "nickname2", "서울시 중구 뭐동");

            em.persist(user1);
            em.persist(user2);

            em.flush();
            em.clear();


            Post post1 = new Post(user1, "title1", 1000, "content1", 1, "서울시 중구 띵동", false);
            Post post2 = new Post(user1, "title2", 2000, "content2", 2, "서울시 중구 띵동", false);
            Post post3 = new Post(user2, "title3", 2000, "content2", 2, "서울시 중구 띵동", false);

            em.persist(post1);
            em.persist(post2);
            em.persist(post3);

            em.flush();
            em.clear();

        }
    }
}
