package com.example.goguma.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Post extends Timestamped {
    @Id // ID 값, Primary Key로 사용하겠다는 뜻입니다.
    @GeneratedValue// 자동 증가 명령입니다.
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int like_count;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private boolean is_sold;
}
