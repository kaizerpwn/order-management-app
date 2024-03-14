package com.ibrahimokic.ordermanagement.mapper;

import com.ibrahimokic.ordermanagement.domain.dto.AddressDto;
import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.domain.entity.Order;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.mapper.impl.OrderMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderMapperImplTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderMapperImpl orderMapper;

    @Test
    void testMapToOrderDto() {
        // Given
        User user = User.builder().userId(101L).build();
        Address deliveryAddress = Address.builder().addressId(1L).build();
        Address sourceAddress = Address.builder().addressId(2L).build();
        Order order = Order.builder()
                .orderId(1L)
                .user(user)
                .orderDate(LocalDate.now())
                .totalAmount(BigDecimal.valueOf(100.0))
                .deliveryAddress(deliveryAddress)
                .sourceAddress(sourceAddress)
                .orderItems(Collections.emptyList())
                .build();

        OrderDto expectedOrderDto = OrderDto.builder()
                .userId(101L)
                .orderDate(LocalDate.now())
                .totalAmount(BigDecimal.valueOf(100.0))
                .deliveryAddress(AddressDto.builder().addressId(1L).build())
                .sourceAddress(AddressDto.builder().addressId(2L).build())
                .orderItems(Collections.emptyList())
                .build();

        when(modelMapper.map(order, OrderDto.class)).thenReturn(expectedOrderDto);

        // When
        OrderDto result = orderMapper.mapTo(order);

        // Then
        assertEquals(expectedOrderDto, result);
    }

    @Test
    void testMapFromOrderDto() {
        // Given
        AddressDto deliveryAddressDto = AddressDto.builder().addressId(1L).build();
        AddressDto sourceAddressDto = AddressDto.builder().addressId(2L).build();
        OrderDto orderDto = OrderDto.builder()
                .userId(101L)
                .orderDate(LocalDate.now())
                .totalAmount(BigDecimal.valueOf(100.0))
                .deliveryAddress(deliveryAddressDto)
                .sourceAddress(sourceAddressDto)
                .orderItems(Collections.emptyList())
                .build();

        User user = User.builder().userId(101L).build();
        Address deliveryAddress = Address.builder().addressId(1L).build();
        Address sourceAddress = Address.builder().addressId(2L).build();
        Order expectedOrder = Order.builder()
                .user(user)
                .orderDate(LocalDate.now())
                .totalAmount(BigDecimal.valueOf(100.0))
                .deliveryAddress(deliveryAddress)
                .sourceAddress(sourceAddress)
                .orderItems(Collections.emptyList())
                .build();

        when(modelMapper.map(orderDto, Order.class)).thenReturn(expectedOrder);

        // When
        Order result = orderMapper.mapFrom(orderDto);

        // Then
        assertEquals(expectedOrder, result);
    }
}
