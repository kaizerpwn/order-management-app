package com.ibrahimokic.ordermanagement.service.impl;

import com.ibrahimokic.ordermanagement.domain.dto.AddressDto;
import com.ibrahimokic.ordermanagement.domain.dto.OrderItemDto;
import com.ibrahimokic.ordermanagement.domain.entity.*;
import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import com.ibrahimokic.ordermanagement.mapper.Mapper;
import com.ibrahimokic.ordermanagement.repository.*;
import com.ibrahimokic.ordermanagement.service.OrderService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final Mapper<Order, OrderDto> orderMapper;
    private final Mapper<Address, AddressDto> addressMapper;
    private final Mapper<OrderItem, OrderItemDto> orderItemMapper;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderItemRepository orderItemRepository,
            Mapper<Order, OrderDto> orderMapper,
            Mapper<Address, AddressDto> addressMapper,
            Mapper<OrderItem, OrderItemDto> orderItemMapper,
            AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderMapper = orderMapper;
        this.addressMapper = addressMapper;
        this.orderItemMapper = orderItemMapper;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<OrderDto> getAllOrdersWithDetails() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> orderDtoList = new ArrayList<>();

        for (Order order : orders) {
            OrderDto orderDto = orderMapper.mapTo(order);
            orderDtoList.add(orderDto);
        }

        return orderDtoList;
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            return orderMapper.mapTo(order);
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteOrderById(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            orderRepository.deleteById(orderId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> createNewOrder(@Valid OrderDto orderDto) {
        try {
            Order order = orderMapper.mapFrom(orderDto);
            Optional<User> optionalUser = userRepository.findById(orderDto.getUserId());

            if (optionalUser.isPresent()) {
                order.setUser(optionalUser.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User with ID " + orderDto.getUserId() + " not found.");
            }

            Address deliveryAddress = addressMapper.mapFrom(orderDto.getDeliveryAddress());
            Address sourceAddress = addressMapper.mapFrom(orderDto.getSourceAddress());

            addressRepository.save(deliveryAddress);
            addressRepository.save(sourceAddress);

            order.setDeliveryAddress(deliveryAddress);
            order.setSourceAddress(sourceAddress);

            order.setOrderItems(null);

            orderRepository.save(order);

            List<OrderItem> orderItems = new ArrayList<>();
            for(OrderItemDto orderItemDto : orderDto.getOrderItems()) {
                OrderItem orderItem = orderItemMapper.mapFrom(orderItemDto);
                orderItem.setOrder(order);

                Optional<Product> productItem = productRepository.findById(orderItemDto.getProductId());

                if(productItem.isPresent()) {
                    orderItem.setProduct(productItem.get());
                    orderItemRepository.save(orderItem);
                    orderItems.add(orderItem);
                }
            }

            order.setOrderItems(orderItems);
            orderRepository.save(order);

            return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.mapTo(order));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating order: " + e.getMessage());
        }
    }
}
