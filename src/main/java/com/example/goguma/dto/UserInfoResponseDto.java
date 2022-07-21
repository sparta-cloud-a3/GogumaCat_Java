package com.example.goguma.dto;

import com.example.goguma.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserInfoResponseDto {
    private Long id;
    private String nickname;
    private List<OrderResponseDto> orders;

    public UserInfoResponseDto(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public void addOrders(List<OrderResponseDto> orders) {
        this.orders = orders;
    }
}
