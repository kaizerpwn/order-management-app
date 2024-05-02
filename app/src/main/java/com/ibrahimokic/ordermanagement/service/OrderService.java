package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import com.ibrahimokic.ordermanagement.domain.entity.Order;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrdersWithDetails();
    List<Order> getAllUsersOrders(User user);
    List<Order> getAllOrders();
    OrderDto getOrderById(Long orderId);
    boolean deleteOrderById(Long orderId);
    ResponseEntity<?> createNewOrder(OrderDto orderDto);
    ResponseEntity<?> updateOrder(Long orderId, OrderDto orderDto);
}
