package com.example.goguma.model;

import com.example.goguma.dto.PostRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private String date;

    @Column(nullable = false)
    private boolean isSold;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostImg> postImgs = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.MERGE)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    public Post(String title, int price, String content, String address, String date) {
        this.title = title;
        this.price = price;
        this.content = content;
        this.address = address;
        this.date = date;
        this.likeCount = 0;
        this.isSold = false;
    }

    public void addUser(User user) {
        this.user = user;
    }

    public void addPostImg(PostImg postImg) {
        postImg.addPost(this);
        postImgs.add(postImg);
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.addPost(this);
    }

    public void addChatRoom(ChatRoom chatRoom) {
        chatRooms.add(chatRoom);
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

    public void sold(boolean isSold) {
        this.isSold = isSold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return getPrice() == post.getPrice() && getLikeCount() == post.getLikeCount() && isSold() == post.isSold() && Objects.equals(getId(), post.getId()) && Objects.equals(getUser(), post.getUser()) && Objects.equals(getTitle(), post.getTitle()) && Objects.equals(getContent(), post.getContent()) && Objects.equals(getAddress(), post.getAddress()) && Objects.equals(getDate(), post.getDate()) && Objects.equals(getPostImgs(), post.getPostImgs());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getTitle(), getPrice(), getContent(), getLikeCount(), getAddress(), getDate(), isSold(), getPostImgs());
    }

}
