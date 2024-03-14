package com.ibrahimokic.ordermanagement.mapper.impl;

import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import com.ibrahimokic.ordermanagement.domain.dto.OrderItemDto;
import com.ibrahimokic.ordermanagement.domain.entity.OrderItem;
import com.ibrahimokic.ordermanagement.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapperImpl implements Mapper<OrderItem, OrderItemDto> {
    private ModelMapper modelMapper;
    public OrderItemMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public OrderItemDto mapTo(OrderItem orderItem) {
        return modelMapper.map(orderItem, OrderItemDto.class);
    }
    @Override
    public OrderItem mapFrom(OrderItemDto orderItemDto) {
        return modelMapper.map(orderItemDto, OrderItem.class);
    }
}
