package com.example.goguma.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
public class Order extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private String startDate;

    private String endDate;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public Order(String startDate, String endDate, int price, Post post, User user) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.post = post;
        this.user = user;
    }
}
