package com.ibrahimokic.ordermanagement.utils;

import com.ibrahimokic.ordermanagement.controller.dto.CreateOrderItemDto;
import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.domain.entity.Order;
import com.ibrahimokic.ordermanagement.domain.entity.OrderItem;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.domain.dto.AddressDto;
import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import com.ibrahimokic.ordermanagement.domain.dto.OrderItemDto;
import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.repository.AddressRepository;
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

        AddressDto deliveryAddressDto = convertAddressToDto(order.getDeliveryAddress());
        AddressDto sourceAddressDto = convertAddressToDto(order.getSourceAddress());

        orderDto.setDeliveryAddress(deliveryAddressDto);
        orderDto.setSourceAddress(sourceAddressDto);

        List<CreateOrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            //CreateOrderItemDto orderItemDto = convertOrderItemToCreateDto(orderItem);
            //orderItemDtos.add(orderItemDto);
        }

        orderDto.setOrderItems(orderItemDtos);

        return orderDto;
    }

    private static AddressDto convertAddressToDto(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setStreet(address.getStreet());
        addressDto.setCity(address.getCity());
        addressDto.setCountry(address.getCountry());
        addressDto.setZip(address.getZip());

        return addressDto;
    }

    public static OrderItemDto convertOrderItemToDto(CreateOrderItemDto orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(orderItem.getProductId());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setItemPrice(orderItem.getItemPrice());

        return orderItemDto;
    }
}
