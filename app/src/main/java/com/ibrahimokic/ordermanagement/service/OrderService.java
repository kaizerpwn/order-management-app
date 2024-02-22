package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.Order;
import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import com.ibrahimokic.ordermanagement.repositories.AddressRepository;
import com.ibrahimokic.ordermanagement.repositories.OrderRepository;
import com.ibrahimokic.ordermanagement.repositories.UserRepository;
import com.ibrahimokic.ordermanagement.utils.ValueConverters;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    public List<OrderDto> getAllOrdersWithDetails() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> orderDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderDto orderDto = ValueConverters.convertOrderToDto(order);
            orderDtos.add(orderDto);
        }

        return orderDtos;
    }

    public OrderDto getOrderById(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            return ValueConverters.convertOrderToDto(order);
        } else {
            return null;
        }
    }

    public boolean deleteOrderById(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            orderRepository.deleteById(orderId);
            return true;
        } else {
            return false;
        }
    }

}
