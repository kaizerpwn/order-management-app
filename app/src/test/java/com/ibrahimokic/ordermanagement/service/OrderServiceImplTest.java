package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.dto.AddressDto;
import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import com.ibrahimokic.ordermanagement.domain.dto.OrderItemDto;
import com.ibrahimokic.ordermanagement.domain.entity.*;
import com.ibrahimokic.ordermanagement.mapper.impl.AddressMapperImpl;
import com.ibrahimokic.ordermanagement.mapper.impl.OrderItemMapperImpl;
import com.ibrahimokic.ordermanagement.mapper.impl.OrderMapperImpl;
import com.ibrahimokic.ordermanagement.repository.*;
import com.ibrahimokic.ordermanagement.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapperImpl addressMapper;

    @Mock
    private OrderItemMapperImpl orderItemMapper;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapperImpl orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testGetAllOrdersWithDetails() {
        Order order = Order.builder().build();
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
        Order order = Order.builder().build();
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
        Order order = Order.builder().build();

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
        Order order = Order.builder().build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        boolean result = orderService.deleteOrderById(orderId);

        assertTrue(result);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    public void testCreateNewOrderShouldFail() {
        User user = User.builder()
                .userId(1L)
                .username("testUser")
                .password("password")
                .email("test@example.com")
                .role("ROLE_USER")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();

        Product product = Product.builder()
                .productName("Chair")
                .price(new BigDecimal(400))
                .productId(1L)
                .availableQuantity(40)
                .availableFrom(LocalDate.of(2024, 4, 20))
                .availableUntil(LocalDate.of(2024, 8, 30))
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        AddressDto address = AddressDto.builder()
                .zip("75000")
                .street("Slatina 15")
                .city("Tuzla")
                .country("Bosnia and Herzegovina")
                .build();

        OrderDto orderDto = OrderDto.builder()
                .orderDate(LocalDate.now())
                .totalAmount(BigDecimal.ZERO)
                .deliveryAddress(address)
                .sourceAddress(address)
                .userId(user.getUserId())
                .build();

        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        OrderItemDto orderItemDto = OrderItemDto.builder().build();
        orderItemDto.setProductId(1L);
        orderItemDto.setQuantity(1);
        orderItemDto.setItemPrice(new BigDecimal(400));
        orderItemDtos.add(orderItemDto);
        orderDto.setOrderItems(orderItemDtos);

        Optional<OrderDto> result = orderService.createNewOrder(orderDto);

        assertNull(result.get().getDeliveryAddress().getAddressId());
    }

    @Test
    public void testCreateNewOrderShouldSuccess() {
        User user = User.builder()
                .userId(1L)
                .username("testUser")
                .password("password")
                .email("test@example.com")
                .role("ROLE_USER")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();

        Product product = Product.builder()
                .productName("Chair")
                .price(new BigDecimal(400))
                .productId(1L)
                .availableQuantity(40)
                .availableFrom(LocalDate.of(2024, 4, 20))
                .availableUntil(LocalDate.of(2024, 8, 30))
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        AddressDto addressDto = AddressDto.builder()
                .zip("string")
                .street("string")
                .city("string")
                .country("string")
                .build();

        OrderDto orderDto = OrderDto.builder()
                .orderDate(LocalDate.now())
                .totalAmount(BigDecimal.ZERO)
                .deliveryAddress(addressDto)
                .sourceAddress(addressDto)
                .userId(user.getUserId())
                .build();

        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        OrderItemDto orderItemDto = OrderItemDto.builder().build();
        orderItemDto.setProductId(1L);
        orderItemDto.setQuantity(1);
        orderItemDto.setItemPrice(new BigDecimal(400));
        orderItemDtos.add(orderItemDto);
        orderDto.setOrderItems(orderItemDtos);

        Optional<OrderDto> result = orderService.createNewOrder(orderDto);

        assertTrue(result.isPresent());
    }

    @Test
    public void testUpdateOrderShouldSuccess() {
        User user = User.builder()
                .userId(1L)
                .username("testUser")
                .password("password")
                .email("test@example.com")
                .role("ROLE_USER")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();

        Product product = Product.builder()
                .productName("Chair")
                .price(new BigDecimal(400))
                .productId(1L)
                .availableQuantity(40)
                .availableFrom(LocalDate.of(2024, 4, 20))
                .availableUntil(LocalDate.of(2024, 8, 30))
                .build();

        Address addressDto = Address.builder()
                .zip("string")
                .street("string")
                .city("string")
                .country("string")
                .build();

        Order order = Order.builder()
                .orderId(1L)
                .orderDate(LocalDate.now())
                .totalAmount(BigDecimal.ZERO)
                .deliveryAddress(addressDto)
                .sourceAddress(addressDto)
                .user(user)
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = OrderItem.builder().build();
        orderItem.setProduct(product);
        orderItem.setQuantity(1);
        orderItem.setItemPrice(new BigDecimal(400));
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);

        Mockito.lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.lenient().when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDto orderDto = OrderDto.builder()
                .userId(1L)
                .orderDate(LocalDate.of(2024,6,5))
                .sourceAddress(AddressDto.builder().zip("75000").street("Slatina 20").country("Bosnia and Herzegovina").city("Tuzla").build())
                .deliveryAddress(AddressDto.builder().zip("75000").street("Slatina 20").country("Bosnia and Herzegovina").city("Tuzla").build())
                .orderItems(Collections.emptyList())
                .build();

        boolean updatedOrder = orderService.updateOrder(1L, orderDto);
        assertTrue(updatedOrder);
    }
}
