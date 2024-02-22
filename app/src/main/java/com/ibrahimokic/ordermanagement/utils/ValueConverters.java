package com.ibrahimokic.ordermanagement.utils;

import com.ibrahimokic.ordermanagement.domain.Address;
import com.ibrahimokic.ordermanagement.domain.Order;
import com.ibrahimokic.ordermanagement.domain.OrderItem;
import com.ibrahimokic.ordermanagement.domain.User;
import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import com.ibrahimokic.ordermanagement.domain.dto.OrderItemDto;
import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.repositories.AddressRepository;
import com.ibrahimokic.ordermanagement.service.ProductService;

import java.util.ArrayList;
import java.util.List;

public class ValueConverters {

    private final AddressRepository addressRepository;
    private final ProductService productService;

    public ValueConverters(AddressRepository addressRepository, ProductService productService) {
        this.addressRepository = addressRepository;
        this.productService = productService;
    }

    public static User convertDtoToUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setBirthDate(userDto.getBirthDate());

        Address address = new Address();
        address.setStreet(userDto.getAddressStreet());
        address.setZip(userDto.getAddressZip());
        address.setCity(userDto.getAddressCity());
        address.setCountry(userDto.getAddressCountry());
        user.setAddress(address);

        return user;
    }

    public static OrderDto convertOrderToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(order.getUser().getUserId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setDeliveryAddress(order.getDeliveryAddress());
        orderDto.setSourceAddress(order.getSourceAddress());

        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderItemDto orderItemDto = convertOrderItemToDto(orderItem);
            orderItemDtos.add(orderItemDto);
        }

        orderDto.setOrderItems(orderItemDtos);

        return orderDto;
    }

    public static OrderItemDto convertOrderItemToDto(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setOrderId(orderItem.getOrder().getOrderId());
        orderItemDto.setProductId(orderItem.getProduct().getProductId());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setItemPrice(orderItem.getItemPrice());

        return orderItemDto;
    }
}
