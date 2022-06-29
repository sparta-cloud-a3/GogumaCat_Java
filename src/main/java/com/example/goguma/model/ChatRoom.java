package com.example.goguma.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChatRoom {

    @Id
    private String roomId;
    private String roomName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name= "user_id") //일대일관계의 경우 fk가 어디있어도 상관없지만 활용 빈도가 높은곳에 많이 넣음.
    private User user;

    public static ChatRoom create(String name) {
        ChatRoom room = new ChatRoom();
        room.roomId = UUID.randomUUID().toString();
        room.roomName = name;
        return room;
    }

    public void addUser(User user) {
        this.user = user;
    }
}
