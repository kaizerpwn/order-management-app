package com.ibrahimokic.ordermanagement.mapper.impl;

import com.ibrahimokic.ordermanagement.domain.dto.OrderItemDto;
import com.ibrahimokic.ordermanagement.domain.entity.OrderItem;
import com.ibrahimokic.ordermanagement.mapper.Mapper;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderItemMapperImpl implements Mapper<OrderItem, OrderItemDto> {
    private final ModelMapper modelMapper;
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

    @Override
    public List<OrderItem> mapListToEntityList(List<OrderItemDto> orderItemDtos) {
        return orderItemDtos.stream()
                .map(this::mapFrom)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderItemDto> mapDtoListToEntityList(@Valid List<OrderItemDto> orderItems) {
        return orderItems.stream()
                .map(orderItemDto -> mapTo(mapFrom(orderItemDto)))
                .collect(Collectors.toList());
    }
}
