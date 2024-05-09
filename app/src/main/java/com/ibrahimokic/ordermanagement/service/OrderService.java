package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import com.ibrahimokic.ordermanagement.domain.entity.Order;
import com.ibrahimokic.ordermanagement.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderDto> getAllOrdersWithDetails();
    List<Order> getAllUsersOrders(User user);
    List<Order> getAllOrders();
    OrderDto getOrderById(Long orderId);
    boolean deleteOrderById(Long orderId);
    Optional<OrderDto> createNewOrder(OrderDto orderDto);
    boolean updateOrder(Long orderId, OrderDto orderDto);
}
