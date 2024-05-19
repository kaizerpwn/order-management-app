package com.ibrahimokic.ordermanagement.utils;

import com.ibrahimokic.ordermanagement.domain.dto.AddressDto;
import com.ibrahimokic.ordermanagement.domain.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UtilsTest {
        @Test
        void clearConsole_shouldClearConsoleForSpecifiedNumberOfLines() {
                final int lines = 5;
                Utils.clearConsole(lines);
        }

        @Test
        void promptUserInput_shouldPromptUserToEnterInputForSpecifiedFieldName() {
                Scanner scanner = mock(Scanner.class);
                String fieldName = "TestField";
                String userInput = "TestInput";
                when(scanner.nextLine()).thenReturn(userInput);
                String input = Utils.promptUserInput(scanner, fieldName);

                assertEquals(userInput, input);
        }

        @Test
        void formatAddress_shouldReturnFormattedAddressString() {
                Address address = Address.builder()
                                .street("123 Main St")
                                .city("New York")
                                .zip("12345")
                                .country("USA")
                                .build();
                String expectedFormattedAddress = "123 Main St, 12345, New York, USA";

                String formattedAddress = Utils.formatAddress(address);

                assertEquals(expectedFormattedAddress, formattedAddress);
        }

        @Test
        void getBindingResults_shouldReturnNullIfBindingResultHasNoErrors() {
                BindingResult bindingResult = mock(BindingResult.class);
                when(bindingResult.hasErrors()).thenReturn(false);

                Object responseEntity = Utils.getBindingResults(bindingResult);

                assertNull(responseEntity);
        }

        @Test
        void getBindingResults_shouldReturnErrorResponseIfBindingResultHasErrors() {
                BindingResult bindingResult = mock(BindingResult.class);
                when(bindingResult.hasErrors()).thenReturn(true);
                FieldError fieldError = new FieldError("objectName", "fieldName", "error message");
                when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

                Object responseEntity = Utils.getBindingResults(bindingResult);

                assertTrue(responseEntity instanceof ResponseEntity);
                assertNotNull(((ResponseEntity<?>) responseEntity).getBody());
        }

        @Test
        void getValidInput_shouldPromptUserForValidInputAndReturnInRange() {
                final int max = 5;
                Scanner scanner = mock(Scanner.class);
                when(scanner.hasNextInt()).thenReturn(true);
                when(scanner.nextInt()).thenReturn(3);

                int input = Utils.getValidInput(scanner, max);

                assertEquals(3, input);
        }

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
