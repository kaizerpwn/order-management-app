package com.ibrahimokic.ordermanagement.mapper;

import com.ibrahimokic.ordermanagement.domain.dto.OrderItemDto;
import com.ibrahimokic.ordermanagement.domain.entity.OrderItem;
import com.ibrahimokic.ordermanagement.domain.entity.Product;
import com.ibrahimokic.ordermanagement.mapper.impl.OrderItemMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class OrderItemMapperImplTest {

    private OrderItemMapperImpl orderItemMapper;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        orderItemMapper = new OrderItemMapperImpl(modelMapper);
    }

    @Test
    void testMapFrom() {
        Product product = mock(Product.class);
        OrderItemDto orderItemDto = OrderItemDto.builder()
                .productId(product.getProductId())
                .quantity(2)
                .itemPrice(new BigDecimal(50))
                .build();

        OrderItem orderItem = orderItemMapper.mapFrom(orderItemDto);

        assertNotNull(orderItem);
        assertEquals(orderItemDto.getProductId(), orderItem.getProduct().getProductId());
        assertEquals(orderItemDto.getQuantity(), orderItem.getQuantity());
        assertEquals(orderItemDto.getItemPrice(), orderItem.getItemPrice());
    }

    @Test
    void testMapListToEntityList() {
        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        orderItemDtos.add(OrderItemDto.builder().productId(product1.getProductId()).quantity(2).itemPrice(new BigDecimal(50)).build());
        orderItemDtos.add(OrderItemDto.builder().productId(product2.getProductId()).quantity(3).itemPrice(new BigDecimal(60)).build());

        List<OrderItem> orderItems = orderItemMapper.mapListToEntityList(orderItemDtos);

        assertNotNull(orderItems);
        assertEquals(orderItemDtos.size(), orderItems.size());

        for (int i = 0; i < orderItemDtos.size(); i++) {
            assertEquals(orderItemDtos.get(i).getProductId(), orderItems.get(i).getProduct().getProductId());
            assertEquals(orderItemDtos.get(i).getQuantity(), orderItems.get(i).getQuantity());
            assertEquals(orderItemDtos.get(i).getItemPrice(), orderItems.get(i).getItemPrice());
        }
    }
}