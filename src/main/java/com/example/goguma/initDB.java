//package com.example.goguma;
//
//import com.example.goguma.model.Post;
//import com.example.goguma.model.PostImg;
//import com.example.goguma.model.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//import javax.persistence.EntityManager;
//
//@Component
//@RequiredArgsConstructor
//public class initDB {
//    private final EntityManager em;
//
//    private final InitService initService;
//
//    @PostConstruct
//    public void init() {
//        initService.dbInit1();
//    }
//
//
//    @Component
//    @Transactional
//    @RequiredArgsConstructor
//    static class InitService {
//        private final EntityManager em;
//        private final PasswordEncoder passwordEncoder;
//
//        public void dbInit1() {
//            String pw = passwordEncoder.encode("aaaa");
//
//            User user1 = new User("user1", pw, "호구마", "서울시 중구 뭐동");
//            User user2 = new User("user2", pw, "고냥이", "서울시 중구 뭐동");
//            User user3 = new User("user3", pw, "스마일", "서울시 중구 뭐동");
//
//            em.persist(user1);
//            em.persist(user2);
//            em.persist(user3);
//
//            em.flush();
//            em.clear();
//
//
//            Post post1 = new Post("title1", 1000, "content1", "서울 강남구 신사동 537-5", "2022-6-30 ~2022-7-4");
//            post1.addUser(user1);
//            Post post2 = new Post("title2", 2000, "content2", "서울 강남구 신사동 537-5","2022-6-30 ~2022-7-4");
//            post2.addUser(user2);
//            Post post3 = new Post("title3", 2000, "content2", "서울 강남구 신사동 537-5", "2022-6-30 ~2022-7-4");
//            post3.addUser(user3);
//
//            em.persist(post1);
//            em.persist(post2);
//            em.persist(post3);
//
//            em.flush();
//            em.clear();
//
//            PostImg postImg1 = new PostImg("https://opgg-com-image.akamaized.net/attach/images/20200415055358.1141863.jpg");
//            postImg1.addPost(post1);
//            PostImg postImg2 = new PostImg("http://50.76.59.227/files/testing_image.jpg");
//            postImg2.addPost(post1);
//            PostImg postImg3 = new PostImg("https://helpx.adobe.com/content/dam/help/en/photoshop/how-to/compositing/jcr%3acontent/main-pars/image/compositing_1408x792.jpg");
//            postImg3.addPost(post2);
//            PostImg postImg4 = new PostImg("https://helpx.adobe.com/content/dam/help/en/photoshop/how-to/compositing/jcr%3acontent/main-pars/image/compositing_1408x792.jpg");
//            postImg4.addPost(post3);
//
//            em.persist(postImg1);
//            em.persist(postImg2);
//            em.persist(postImg3);
//            em.persist(postImg4);
//
//            em.flush();
//            em.clear();
//        }
//    }
//}