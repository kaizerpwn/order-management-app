package com.ibrahimokic.ordermanagement.service.impl;

import com.ibrahimokic.ordermanagement.domain.dto.OrderItemDto;
import com.ibrahimokic.ordermanagement.domain.entity.*;
import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import com.ibrahimokic.ordermanagement.mapper.impl.AddressMapperImpl;
import com.ibrahimokic.ordermanagement.mapper.impl.OrderItemMapperImpl;
import com.ibrahimokic.ordermanagement.mapper.impl.OrderMapperImpl;
import com.ibrahimokic.ordermanagement.repository.*;
import com.ibrahimokic.ordermanagement.service.OrderService;
import com.ibrahimokic.ordermanagement.utils.Utils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final OrderMapperImpl orderMapper;
    private final AddressMapperImpl addressMapper;
    private final OrderItemMapperImpl orderItemMapper;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    @Override
    public List<OrderDto> getAllOrdersWithDetails() {
        try {
            List<Order> orders = orderRepository.findAll();
            List<OrderDto> orderDtoList = new ArrayList<>();

            for (Order order : orders) {
                OrderDto orderDto = orderMapper.mapTo(order);
                orderDtoList.add(orderDto);
            }

            return orderDtoList;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all users orders: " + e.getMessage());
        }
    }

    @Override
    public List<Order> getAllUsersOrders(User user) {
        try {
            return orderRepository.getAllUsersOrders(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all users orders: " + e.getMessage());
        }
    }

    @Override
    public List<Order> getAllOrders() {
        try {
            return orderRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all orders: " + e.getMessage());
        }
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        try {
            Optional<Order> orderOptional = orderRepository.findById(orderId);

            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                return orderMapper.mapTo(order);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get order by ID: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteOrderById(Long orderId) {
        try {
            Optional<Order> optionalOrder = orderRepository.findById(orderId);

            if (optionalOrder.isPresent()) {
                orderRepository.deleteById(orderId);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete order by ID: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<OrderDto> createNewOrder(@Valid OrderDto orderDto) {
        try {
            Order order = Order.builder()
                    .user(User.builder().userId(orderDto.getUserId()).build())
                    .deliveryAddress(addressMapper.mapFrom(orderDto.getDeliveryAddress()))
                    .sourceAddress(addressMapper.mapFrom(orderDto.getSourceAddress()))
                    .orderDate(orderDto.getOrderDate())
                    .orderItems(null)
                    .totalAmount(new BigDecimal(0))
                    .build();

            Optional<User> optionalUser = userRepository.findById(orderDto.getUserId());

            if (optionalUser.isPresent()) {
                order.setUser(optionalUser.get());
            } else {
                throw new RuntimeException("User with ID " + orderDto.getUserId() + " not found.");
            }

            addressRepository.save(order.getDeliveryAddress());
            addressRepository.save(order.getSourceAddress());

            orderRepository.save(order);

            List<OrderItem> orderItems = new ArrayList<>();

            for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
                OrderItem orderItem = OrderItem.builder()
                        .order(order)
                        .product(productRepository.findById(orderItemDto.getProductId()).orElse(null))
                        .quantity(orderItemDto.getQuantity())
                        .itemPrice(orderItemDto.getItemPrice())
                        .build();

                Optional<Product> productItem = productRepository.findById(orderItemDto.getProductId());

                if (productItem.isPresent()) {
                    if(orderItemDto.getQuantity() != 0) {
                        if (!Utils.checkProductQuantity(productItem.get(), orderItemDto.getQuantity())) {
                            throw new RuntimeException("Quantity available for product "+ productItem.get().getProductId() +" is '" + productItem.get().getAvailableQuantity() + "', not '" + orderItemDto.getQuantity() + "'.");
                        }
                        if (!Utils.checkProductAvailability(productItem.get())) {
                            throw new RuntimeException("Product "+ productItem.get().getProductId() +" is not currently available, it is available from the date " + productItem.get().getAvailableFrom() + " to "+ productItem.get().getAvailableUntil() + ".");
                        }

                        orderItemRepository.save(orderItem);
                        orderItems.add(orderItem);

                        productItem.get().setAvailableQuantity(productItem.get().getAvailableQuantity() - orderItem.getQuantity());
                        productRepository.save(productItem.get());
                    } else {
                        throw new RuntimeException("Product with id " + orderItemDto.getProductId() +" can't have zero quantity.");
                    }
                }
            }

            order.setOrderItems(orderItems);
            order.setTotalAmount(Utils.calculateTotalProductsPriceAmount(orderItems));

            orderRepository.save(order);

            return Optional.ofNullable(orderMapper.mapTo(order));
        } catch (Exception e) {
            throw new RuntimeException("Error creating order: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateOrder(Long orderId, @Valid OrderDto orderDto) {
        try {
            Optional<Order> retrievedOrderFromDatabase = orderRepository.findById(orderId);

            if (retrievedOrderFromDatabase.isPresent()) {
                Optional<User> optionalUser = userRepository.findById(orderDto.getUserId());

                if (optionalUser.isPresent()) {
                    if (Utils.checkIfUserIdIsDifferent(optionalUser.get(), retrievedOrderFromDatabase.get())) {
                        retrievedOrderFromDatabase.get().setUser(optionalUser.get());
                    }

                    if (Utils.checkIfAddressIsDifferent(retrievedOrderFromDatabase.get().getDeliveryAddress(),
                            orderDto.getDeliveryAddress())) {
                        Address newAddress = addressMapper.mapFrom(orderDto.getDeliveryAddress());
                        addressRepository.save(newAddress);
                        retrievedOrderFromDatabase.get().setDeliveryAddress(newAddress);
                    }

                    if (Utils.checkIfAddressIsDifferent(retrievedOrderFromDatabase.get().getSourceAddress(),
                            orderDto.getSourceAddress())) {
                        Address newAddress = addressMapper.mapFrom(orderDto.getSourceAddress());
                        addressRepository.save(newAddress);
                        retrievedOrderFromDatabase.get().setSourceAddress(newAddress);
                    }

                    if (Utils.checkIfOrderItemsAreDifferent(retrievedOrderFromDatabase.get().getOrderItems(),
                            orderItemMapper.mapListToEntityList(orderDto.getOrderItems()))) {

                        List<OrderItem> orderItems = new ArrayList<>();

                        for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
                            OrderItem orderItem = OrderItem.builder()
                                    .order(retrievedOrderFromDatabase.get())
                                    .product(productRepository.findById(orderItemDto.getProductId()).orElse(null))
                                    .quantity(orderItemDto.getQuantity())
                                    .itemPrice(orderItemDto.getItemPrice())
                                    .build();

                            Optional<Product> productItem = productRepository.findById(orderItemDto.getProductId());

                            if (productItem.isPresent()
                                    && productItem.get().getAvailableQuantity() >= orderItem.getQuantity()) {
                                orderItemRepository.save(orderItem);
                                orderItems.add(orderItem);

                                retrievedOrderFromDatabase.get().getOrderItems().add(orderItem);
                                orderItem.setOrder(retrievedOrderFromDatabase.get());

                                productItem.get().setAvailableQuantity(
                                        productItem.get().getAvailableQuantity() - orderItem.getQuantity());
                                productRepository.save(productItem.get());
                            }
                        }

                        retrievedOrderFromDatabase.get().getOrderItems().clear();
                        retrievedOrderFromDatabase.get().getOrderItems().addAll(orderItems);
                        retrievedOrderFromDatabase.get()
                                .setTotalAmount(Utils.calculateTotalProductsPriceAmount(orderItems));
                    }

                    orderRepository.save(retrievedOrderFromDatabase.get());

                    return true;
                } else {
                    throw new RuntimeException("User with ID " + orderDto.getUserId() + " not found.");
                }
            }
            else {
                throw new RuntimeException("Order with ID '" + orderId + "' does not exist.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error creating order: " + e.getMessage());
        }
    }
}
