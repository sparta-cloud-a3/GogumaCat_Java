package com.example.goguma.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class PostImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_img_id")
    private Long id;

    @Column(nullable = false)
    private String img_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id", nullable = false)
    private Post post;

    public PostImg(String img_url) {
        this.img_url = img_url;
    }

    public void addPost(Post post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "PostImg{" +
                "id=" + id +
                ", img_url='" + img_url + '\'' +
                ", post=" + post +
                '}';
    }
}
