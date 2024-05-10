package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import com.ibrahimokic.ordermanagement.domain.entity.Order; 
import com.ibrahimokic.ordermanagement.mapper.impl.OrderMapperImpl;
import com.ibrahimokic.ordermanagement.repository.*;
import com.ibrahimokic.ordermanagement.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapperImpl orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testGetAllOrdersWithDetails() {
        Order order = new Order();
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.mapTo(order)).thenReturn(new OrderDto());

        List<OrderDto> result = orderService.getAllOrdersWithDetails();

        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
        verify(orderMapper, times(1)).mapTo(order);
    }

    @Test
    void testGetAllOrders() {
        Order order = new Order();
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderById() {
        Long orderId = 1L;
        Order order = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.mapTo(order)).thenReturn(new OrderDto());

        OrderDto result = orderService.getOrderById(orderId);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderMapper, times(1)).mapTo(order);
    }

    @Test
    void testDeleteOrderById() {
        Long orderId = 1L;
        Order order = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        boolean result = orderService.deleteOrderById(orderId);

        assertTrue(result);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).deleteById(orderId);
    }
}
