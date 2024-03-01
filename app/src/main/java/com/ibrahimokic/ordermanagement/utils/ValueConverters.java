package com.ibrahimokic.ordermanagement.utils;

import com.ibrahimokic.ordermanagement.controller.dto.CreateOrderItemDto;
import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.domain.entity.Order;
import com.ibrahimokic.ordermanagement.domain.entity.OrderItem;
import com.ibrahimokic.ordermanagement.domain.dto.AddressDto;
import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import com.ibrahimokic.ordermanagement.repository.AddressRepository;
import com.ibrahimokic.ordermanagement.service.impl.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class ValueConverters {

    private final AddressRepository addressRepository;
    private final ProductServiceImpl productServiceImpl;

    public ValueConverters(AddressRepository addressRepository, ProductServiceImpl productServiceImpl) {
        this.addressRepository = addressRepository;
        this.productServiceImpl = productServiceImpl;
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
}
