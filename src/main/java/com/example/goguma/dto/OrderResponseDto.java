package com.example.goguma.dto;

import com.example.goguma.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private String startDate;
    private String endDate;
    private int price;
    private OrderPostDto post;
}
