package com.example.goguma.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChatRoom extends Timestamped{

    @Id
    private String roomId;
    private String roomName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="post_id")
    @JsonIgnore
    private Post post;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name= "user_id")
    @JsonIgnore
    private User user;

    public static ChatRoom create(String name) {
        ChatRoom room = new ChatRoom();
        room.roomId = UUID.randomUUID().toString();
        room.roomName = name;
        return room;
    }

    public void addPost(Post post) {
        this.post = post;
        post.addChatRoom(this);
    }

    public void addUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", post=" + post +
                ", user=" + user +
                '}';
    }
}
