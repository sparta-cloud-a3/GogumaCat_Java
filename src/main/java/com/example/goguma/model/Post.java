package com.example.goguma.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Post extends Timestamped {
    @Id // ID 값, Primary Key로 사용하겠다는 뜻입니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private int likeCount;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private boolean is_sold;

    public Post(User user, String title, int price, String content, int likeCount, String address, boolean is_sold) {
        this.user = user;
        this.title = title;
        this.price = price;
        this.content = content;
        this.likeCount = likeCount;
        this.address = address;
        this.is_sold = is_sold;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", content='" + content + '\'' +
                ", likeCount=" + likeCount +
                ", address='" + address + '\'' +
                ", is_sold=" + is_sold +
                '}';
    }
}
