package com.example.goguma.service;

import com.example.goguma.model.ChatRoom;
import com.example.goguma.model.Order;
import com.example.goguma.model.Post;
import com.example.goguma.model.User;
import com.example.goguma.repository.ChatRepository;
import com.example.goguma.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ChatRepository chatRepository;

    public String order(String roomId) {
        ChatRoom chatRoom = chatRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );

        Post post = chatRoom.getPost();

        //판매완료
        post.sold(true);

        String[] dateSplit = post.getDate().split("~");
        String startDate = dateSplit[0].trim();
        String endDate = dateSplit[1].trim();

        int price = post.getPrice();
        User customer = chatRoom.getUser();

        orderRepository.save(new Order(startDate, endDate, price, post, customer));

        //구매자한테 알림

        return customer.getNickname() + "님과 거래가 되었습니다!";
    }
}
