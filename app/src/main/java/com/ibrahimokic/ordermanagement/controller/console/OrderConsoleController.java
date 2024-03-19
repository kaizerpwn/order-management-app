package com.ibrahimokic.ordermanagement.controller.console;

import com.ibrahimokic.ordermanagement.controller.console.ui.ConsoleUserInterface;
import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.domain.entity.Order;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.repository.OrderRepository;
import com.ibrahimokic.ordermanagement.utils.Utils;

import java.util.List;

public class OrderConsoleController extends ConsoleUserInterface {

    private final User loggedUser;
    private final OrderRepository orderRepository;

    public OrderConsoleController(User loggedUser, OrderRepository orderRepository) {
        this.loggedUser = loggedUser;
        this.orderRepository = orderRepository;
    }

    public List<Order> showAllOrdersList() {
        List<Order> orderList = orderRepository.findAll();

        System.out.println("|-------------|---------------------|---------------|---------------------|------------------------------------------|------------------------------------------|");
        System.out.println("|   Order ID  |         User        |  Order Date   |    Total Amount     |             Delivery Address             |              Source Address              |");
        System.out.println("|-------------|---------------------|---------------|---------------------|------------------------------------------|------------------------------------------|");

        for (Order order : orderList) {
            Address deliveryAddress = order.getDeliveryAddress();
            Address sourceAddress = order.getSourceAddress();

            System.out.printf("| %-12s| %s %-12s| %-14s| $%-19s| %-41s| %-41s|%n",
                    order.getOrderId(),
                    order.getUser().getFirstName(),
                    order.getUser().getLastName(),
                    order.getOrderDate(),
                    order.getTotalAmount(),
                    Utils.formatAddress(deliveryAddress),
                    Utils.formatAddress(sourceAddress));
        }

        System.out.println("|-------------|---------------------|---------------|---------------------|------------------------------------------|------------------------------------------|");
        return orderList;
    }

    public void displayAdminOrderManagementMenu() {
        Utils.clearConsole(20);
        consoleHeader();

        System.out.println("[OM-APP]: Welcome, " + loggedUser.getFirstName() + "! Take a moment to choose your next step:");
        System.out.println("1.) List of all orders");
        System.out.println("2.) Return to the main menu");
    }
}
