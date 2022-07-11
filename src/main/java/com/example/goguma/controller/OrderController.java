package com.example.goguma.controller;

import com.example.goguma.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @ResponseBody
    @PostMapping("/order")
    public String order(@RequestParam String roomId) {
        return orderService.order(roomId);
    }
}
