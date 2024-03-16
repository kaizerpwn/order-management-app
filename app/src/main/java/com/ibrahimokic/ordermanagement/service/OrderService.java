package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrdersWithDetails();

    OrderDto getOrderById(Long orderId);

    boolean deleteOrderById(Long orderId);

    ResponseEntity<?> createNewOrder(OrderDto orderDto);
    ResponseEntity<?> updateOrder(Long orderId, OrderDto orderDto);
}
