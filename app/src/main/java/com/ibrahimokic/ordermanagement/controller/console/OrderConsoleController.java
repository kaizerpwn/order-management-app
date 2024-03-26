package com.ibrahimokic.ordermanagement.controller.console;

import com.ibrahimokic.ordermanagement.controller.console.ui.ConsoleUserInterface;
import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.domain.entity.Order;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.repository.OrderRepository;
import com.ibrahimokic.ordermanagement.utils.Utils;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class OrderConsoleController extends ConsoleUserInterface {
    private final User loggedUser;
    private final OrderRepository orderRepository;

    public List<Order> showAllOrdersList() {
        List<Order> orderList = orderRepository.findAll();

        System.out.println("|-------------|---------------------|---------------|---------------------|-------------------------------------------------------------|-------------------------------------------------------|");
        System.out.println("|   Order ID  |        User         |  Order Date   |    Total Amount     |                      Delivery Address                       |                     Source Address                    |");
        System.out.println("|-------------|---------------------|---------------|---------------------|-------------------------------------------------------------|-------------------------------------------------------|");

        for (Order order : orderList) {
            Address deliveryAddress = order.getDeliveryAddress();
            Address sourceAddress = order.getSourceAddress();

            System.out.printf("| %-12s| %-20s| %-14s| $%-19s| %-45s| %-45s |%n",
                    order.getOrderId(),
                    order.getUser().getFirstName() + " " + order.getUser().getLastName(),
                    order.getOrderDate(),
                    order.getTotalAmount(),
                    Utils.formatAddress(deliveryAddress),
                    Utils.formatAddress(sourceAddress));
        }

        System.out.println("|-------------|---------------------|---------------|---------------------|-------------------------------------------------------------|-------------------------------------------------------|");
        return orderList;
    }

    public void exportOrdersAsExcelFile() {
        List<Order> orderList = showAllOrdersList();

        System.out.println(">> Enter the file path where you want to export the Excel file:");
        String filePath = scanner.nextLine();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Orders");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Order ID", "User ID", "First Name", "Last Name", "Order Date", "Total Amount",
                    "Delivery Address", "Source Address"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (Order order : orderList) {
                Address deliveryAddress = order.getDeliveryAddress();
                Address sourceAddress = order.getSourceAddress();

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(order.getOrderId());
                row.createCell(1).setCellValue(order.getUser().getUserId());
                row.createCell(2).setCellValue(order.getUser().getFirstName());
                row.createCell(3).setCellValue(order.getUser().getLastName());
                row.createCell(4).setCellValue(order.getOrderDate().toString());
                row.createCell(5).setCellValue("$" + order.getTotalAmount().toString());
                row.createCell(6).setCellValue(Utils.formatAddress(deliveryAddress));
                row.createCell(7).setCellValue(Utils.formatAddress(sourceAddress));
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                System.out.println("Excel file has been successfully exported to: " + filePath);
            }

        } catch (IOException e) {
            System.out.println("An error occurred while exporting the Excel file: " + e.getMessage());
        }
    }

    public void displayAdminOrderManagementMenu() {
        Utils.clearConsole(20);
        consoleHeader();

        System.out.println("[OM-APP]: Welcome, " + loggedUser.getFirstName() + "! Take a moment to choose your next step:");
        System.out.println("1.) List of all orders");
        System.out.println("2.) Export orders as CSV file");
        System.out.println("3.) Return to the main menu");
    }
}
