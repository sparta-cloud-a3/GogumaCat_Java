package com.example.goguma.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "msg_id")
    Long id;


    @Enumerated(value = EnumType.STRING)
    private MessageType type;

    //채팅방 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChatRoom room;

    //보내는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User sender;

    //내용
    private String message;

    public void addRoom(ChatRoom room) {
        this.room = room;
        room.addMessage(this);
    }

    public void addSender(User sender) {
        this.sender = sender;
        sender.addMessages(this);
    }

    public ChatMessage(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }
}
