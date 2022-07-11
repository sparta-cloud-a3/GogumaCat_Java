package com.example.goguma.controller;

import com.example.goguma.jwt.JwtProvider;
import com.example.goguma.model.User;
import com.example.goguma.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final JwtProvider jwtProvider;
    private final OrderService orderService;

    @ResponseBody
    @PostMapping("/order")
    public String updateLike(@RequestParam String roomId) {
        return orderService.order(roomId);
    }
}
