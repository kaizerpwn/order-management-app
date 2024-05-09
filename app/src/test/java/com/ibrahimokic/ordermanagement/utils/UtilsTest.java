package com.ibrahimokic.ordermanagement.utils;

import com.ibrahimokic.ordermanagement.domain.dto.AddressDto;
import com.ibrahimokic.ordermanagement.domain.entity.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {
    @Test
    void calculateTotalProductsPriceAmount_shouldReturnCorrectTotal() {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(OrderItem.builder()
                .product(Product.builder()
                        .productName("Product 1")
                        .price(BigDecimal.TEN)
                        .availableQuantity(2).build())
                .quantity(3)
                .itemPrice(BigDecimal.TEN)
                .build());
        orderItems.add(OrderItem.builder()
                .product(Product.builder()
                        .productName("Product 2")
                        .price(BigDecimal.valueOf(5))
                        .availableQuantity(1).build())
                .itemPrice(BigDecimal.TEN)
                .quantity(2)
                .build());
        BigDecimal expectedTotal = BigDecimal.valueOf(50);
        assertEquals(expectedTotal, Utils.calculateTotalProductsPriceAmount(orderItems));
    }

    @Test
    void checkIfUserIdIsDifferent_shouldReturnTrueForDifferentUserIds() {
        User user1 = User.builder().userId(1L).firstName("John").build();
        User user2 = User.builder().userId(2L).firstName("Jane").build();
        Order order = Order.builder().user(user1).build();
        assertTrue(Utils.checkIfUserIdIsDifferent(user2, order));
    }

    @Test
    void checkIfUserIdIsDifferent_shouldReturnFalseForSameUserIds() {
        User user1 = User.builder().userId(1L).firstName("John").build();
        Order order = Order.builder().user(user1).build();
        assertFalse(Utils.checkIfUserIdIsDifferent(user1, order));
    }

    @Test
    void checkIfAddressIsDifferent_shouldReturnTrueForDifferentAddresses() {
        Address address = Address.builder()
                .street("123 Main St")
                .city("New York")
                .zip("12345")
                .country("USA")
                .build();
        AddressDto addressDto = AddressDto.builder()
                .street("456 Oak St")
                .city("Los Angeles")
                .zip("54321")
                .country("USA")
                .build();
        assertTrue(Utils.checkIfAddressIsDifferent(address, addressDto));
    }

    @Test
    void checkIfAddressIsDifferent_shouldReturnFalseForSameAddresses() {
        Address address = Address.builder()
                .street("123 Main St")
                .city("New York")
                .zip("12345")
                .country("USA")
                .build();
        AddressDto addressDto = AddressDto.builder()
                .street("123 Main St")
                .city("New York")
                .zip("12345")
                .country("USA")
                .build();
        assertFalse(Utils.checkIfAddressIsDifferent(address, addressDto));
    }

    @Test
    void checkIfOrderItemsAreDifferent_shouldReturnFalseForSameItems() {
        List<OrderItem> orderItems1 = new ArrayList<>();
        List<OrderItem> orderItems2 = new ArrayList<>();
        orderItems1.add(OrderItem.builder()
                .product(Product.builder().productName("Product 1").availableQuantity(2).build())
                .quantity(3)
                .build());
        orderItems2.add(OrderItem.builder()
                .product(Product.builder().productName("Product 2").availableQuantity(1).build())
                .quantity(2)
                .build());
        assertFalse(Utils.checkIfOrderItemsAreDifferent(orderItems1, orderItems2));
    }

    @Test
    void checkIfOrderItemsAreDifferent_shouldReturnTrueForDifferentItems() {
        List<OrderItem> orderItems1 = new ArrayList<>();
        List<OrderItem> orderItems2 = new ArrayList<>();
        orderItems1.add(OrderItem.builder()
                .product(Product.builder().productName("Product 1").availableQuantity(2).build())
                .quantity(3)
                .build());
        orderItems2.add(OrderItem.builder()
                .product(Product.builder().productName("Product 1").availableQuantity(2).build())
                .quantity(3)
                .build());
        assertFalse(Utils.checkIfOrderItemsAreDifferent(orderItems1, orderItems2));
    }
}
