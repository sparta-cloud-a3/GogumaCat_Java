package com.example.goguma.controller;

import com.example.goguma.service.OrderService;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public String order(@RequestParam String roomId) {
        JsonObject json = new JsonObject();
        json.addProperty("msg", orderService.order(roomId));
        return json.toString();
    }
}
