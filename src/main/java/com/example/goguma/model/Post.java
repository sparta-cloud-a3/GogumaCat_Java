package com.example.goguma.model;

import com.example.goguma.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
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
    private String date;

    @Column(nullable = false)
    private boolean isSold;

    public Post(User user, String title, int price, String content, int likeCount, String address, String date, boolean isSold) {
        this.user = user;
        this.title = title;
        this.price = price;
        this.content = content;
        this.likeCount = likeCount;
        this.address = address;
        this.date = date;
        this.isSold = isSold;
    }

    public Post(String title, int price, String content, int likeCount, String address, String date,boolean isSold) {
        this.title = title;
        this.price = price;
        this.content = content;
        this.likeCount = likeCount;
        this.address = address;
        this.date = date;
        this.isSold = isSold;
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
                ", isSold=" + isSold +
                '}';
    }

    public void update(PostRequestDto postRequestDto) {
        if(postRequestDto.getTitle() != null) {
            this.title = postRequestDto.getTitle();
        }
        if(postRequestDto.getPrice() != null) {
            this.price = Integer.parseInt(postRequestDto.getPrice().replace(",",""));
        }
        if(postRequestDto.getDate() != null) {
            this.date = postRequestDto.getDate();
        }
        if(postRequestDto.getContent() != null) {
            this.content = postRequestDto.getContent();
        }
        if(postRequestDto.getAddress() != null) {
            this.address = postRequestDto.getAddress();
        }
    }

    public void updateLikeCount(String action) {
        if (action.equals("like")) {
            likeCount++;
        } else {
            likeCount--;
        }
    }
}
