package com.example.goguma;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Like extends Timestamped {
    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private Long id;
//    private Post post;
//    private User user;
}
