package com.example.goguma.repository;

import com.example.goguma.model.Order;
import com.example.goguma.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByIsCheckedAndUserId(boolean isChecked, Long userId);
}
