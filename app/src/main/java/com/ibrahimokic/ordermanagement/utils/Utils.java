package com.ibrahimokic.ordermanagement.utils;

import com.ibrahimokic.ordermanagement.domain.dto.AddressDto;
import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.domain.entity.Order;
import com.ibrahimokic.ordermanagement.domain.entity.OrderItem;
import com.ibrahimokic.ordermanagement.domain.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Utils {
    public static BigDecimal calculateTotalProductsPriceAmount(List<OrderItem> orderItems) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            totalAmount = totalAmount.add(orderItem.getProduct().getPrice());
        }
        return totalAmount;
    }

    public static boolean checkIfUserIdIsDifferent(User user, Order retrievedOrderFromDatabase) {
        return !Objects.equals(user.getUserId(), retrievedOrderFromDatabase.getUser().getUserId());
    }

    public static boolean checkIfAddressIsDifferent(Address address, AddressDto addressDto) {
        if (!Objects.equals(address.getStreet(), addressDto.getStreet()) ||
            !Objects.equals(address.getCity(), addressDto.getCity()) ||
            !Objects.equals(address.getZip(), addressDto.getZip()) ||
            !Objects.equals(address.getCountry(), addressDto.getCountry())) {

            System.out.println(address.getStreet() + " "+ addressDto.getStreet());
            System.out.println(address.getCity() + " "+ addressDto.getCity());
            System.out.println(address.getZip() + " "+ addressDto.getZip());
            System.out.println(address.getCountry() + " "+ addressDto.getCountry());
            return true;
        }
        return false;
    }

    public static boolean checkIfOrderItemsAreDifferent(List<OrderItem> retrievedOrderItems, List<OrderItem> updatedOrderItems) {
        boolean differentOrderItems = false;

        if (retrievedOrderItems.size() != updatedOrderItems.size()) {
            return true;
        }

        for (int i = 0; i < retrievedOrderItems.size(); i++) {
            OrderItem retrievedOrderItem = retrievedOrderItems.get(i);
            OrderItem updatedOrderItem = updatedOrderItems.get(i);

            if (!Objects.equals(retrievedOrderItem.getProduct().getProductId(), updatedOrderItem.getProduct().getProductId())) {
                differentOrderItems = true;
                break;
            }
        }

        return differentOrderItems;
    }

    public static void clearConsole(int lines) {
        for(int i = 0; i < lines; i++) {
            System.out.println("\n");
        }
    }

    public static void returnBackToTheMainMenu(Scanner scanner) {
        System.out.print(">> To return back to the main menu press 'ENTER': ");

        scanner.nextLine();
        scanner.nextLine();
    }

    public static String promptUserInput(Scanner scanner, String fieldName) {
        System.out.print(">> Please enter the "+ fieldName +": ");
        return scanner.nextLine();
    }

    public static String formatAddress(Address address) {
        return String.format("%s, %s, %s, %s", address.getStreet(), address.getZip(), address.getCity(), address.getCountry());
    }
}
