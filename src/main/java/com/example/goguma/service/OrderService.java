package com.example.goguma.service;

import com.example.goguma.dto.OrderPostDto;
import com.example.goguma.dto.OrderResponseDto;
import com.example.goguma.dto.PostImgResponseDto;
import com.example.goguma.dto.PostResponseDto;
import com.example.goguma.exception.NoSuchRoomException;
import com.example.goguma.model.ChatRoom;
import com.example.goguma.model.Order;
import com.example.goguma.model.Post;
import com.example.goguma.model.User;
import com.example.goguma.repository.ChatRepository;
import com.example.goguma.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ChatRepository chatRepository;

    public String order(String roomId) {
        ChatRoom chatRoom = chatRepository.findById(roomId).orElseThrow(
                NoSuchRoomException::new
        );

        Post post = chatRoom.getPost();
        User customer = chatRoom.getUser();

        //주문 저장
        Order order = Order.makeOrder(post);
        customer.addOrder(order);
        post.addOrder(order);
        orderRepository.save(order);

        //구매자한테 알림

        return customer.getNickname() + "님과 거래가 되었습니다!";
    }

    public List<OrderResponseDto> notCheckedOrderList(Long userId) {
        return orderRepository.findAllByUserIdAndIsChecked(userId, false).stream().map(
                o -> {
                    Post p = o.getPost();
                    OrderPostDto orderPostDto = new OrderPostDto(p.getId(), p.getTitle(), p.getUser().getNickname());
                    return new OrderResponseDto(o.getId(), o.getStartDate(), o.getEndDate(), o.getPrice(), orderPostDto);
                }
        ).collect(Collectors.toList());
    }

    public boolean hasOrder(Long userId) {
        return orderRepository.existsByUserIdAndIsChecked(userId, false);
    }
}
