package com.ibrahimokic.ordermanagement.repository;

import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.domain.entity.Order;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void testSaveOrder() {
        User user = User.builder()
                .username("testuser")
                .password("password")
                .email("testuser@example.com")
                .role("ROLE_USER")
                .build();

        User savedUser = userRepository.save(user);

        Address deliveryAddress = Address.builder()
                .city("Sarajevo")
                .zip("71000")
                .country("Bosnia and Herzegovina")
                .street("Delivery Street 123")
                .build();

        Address sourceAddress = Address.builder()
                .city("Tuzla")
                .zip("75000")
                .country("Bosnia and Herzegovina")
                .street("Source Street 456")
                .build();

        Address savedDeliveryAddress = addressRepository.save(deliveryAddress);
        Address savedSourceAddress = addressRepository.save(sourceAddress);

        Order order = Order.builder()
                .user(savedUser)
                .orderDate(LocalDate.now())
                .totalAmount(BigDecimal.TEN)
                .deliveryAddress(savedDeliveryAddress)
                .sourceAddress(savedSourceAddress)
                .build();

        Order savedOrder = orderRepository.save(order);

        assertNotNull(savedOrder.getOrderId());
    }
    @Test
    void testFindById() {
        Long orderId = 1L;

        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        assertFalse(optionalOrder.isPresent());
    }

    @Test
    void testFindAll() {
        testSaveOrder();
        List<Order> orders = orderRepository.findAll();

        assertEquals(1, orders.size());
    }
}
