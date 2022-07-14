package com.example.goguma.model;

import com.example.goguma.dto.ProfileUpdateDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class User extends Timestamped {

    // ID가 자동으로 생성 및 증가합니다.

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private Long id;
    // 반드시 값을 가지도록 합니다.

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private Long kakaoId;

    @Column(nullable = true)
    private String profilePic;

    @Column(nullable = true)
    private String profileInfo;

    @Column(nullable = true)
    private String accessToken;

    @Column(nullable = true)
    private String refreshToken;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    public void addPost(Post post) {
        posts.add(post);
        post.addUser(this);
    }

    public void addLike(Like like) {
        likes.add(like);
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.addCustomer(this);
    }

    public void addChatRoom(ChatRoom chatRoom) {
        chatRooms.add(chatRoom);
    }

    public User(String username, String password, String nickname, String address) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.kakaoId = null;
        this.profilePic = null;
        this.profileInfo = null;
    }

    public User(String username, String password, String nickname, Long kakaoId, String profilePic) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.address = null;
        this.kakaoId = kakaoId;
        this.profilePic = profilePic;
        this.profileInfo = null;
    }


    public void update(ProfileUpdateDto profileUpdateDto,String password,String profilePic){
        this.nickname = profileUpdateDto.getNickname();
        this.password = password;
        this.address = profileUpdateDto.getAddress();
        this.profilePic = profilePic;
        this.profileInfo = profileUpdateDto.getProfileInfo();
    }

    public void accessToken(String accessToken){
        this.accessToken = accessToken;
        System.out.println("this.accessToken : "+this.accessToken);
    }

    public void refreshToken(String refreshToken){
        this.refreshToken = refreshToken;
        System.out.println("this.refreshToken : "+this.refreshToken);
    }
}

