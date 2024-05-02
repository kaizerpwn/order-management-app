package com.ibrahimokic.ordermanagement.adapters;

import com.ibrahimokic.ordermanagement.controller.console.ui.ConsoleUserInterface;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.service.AddressService;
import com.ibrahimokic.ordermanagement.service.OrderService;
import com.ibrahimokic.ordermanagement.service.ProductService;
import com.ibrahimokic.ordermanagement.service.UserService;
import com.ibrahimokic.ordermanagement.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserConsoleAdapter extends ConsoleUserInterface {
    private final UserService userService;
    private final AddressService addressService;
    private final ProductService productService;
    private final OrderService orderService;

    public void userMainForm() {
        Utils.clearConsole(20);
        consoleHeader();

        System.out.println("""
                [OM-APP]: Welcome! Take a moment to choose your next step:
                1.) Log in
                2.) Exit""");

        int choice;
        do {
            System.out.print(">> Please enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter 1 or 2.");
                System.out.print("Enter your choice (1 or 2): ");
                scanner.next();
            }
            choice = scanner.nextInt();
            if (choice < 1 || choice > 2)
                System.out.println("Invalid choice. Please enter 1 or 2.");
        } while (choice < 1 || choice > 2);

        switch (choice) {
            case 1 ->
            {
                scanner.nextLine();
                userLoginForm();
            }
            case 2 -> System.exit(0);
        }

        scanner.close();
    }

    public void userLoginForm() {
        boolean loggedIn = false;
        boolean goToMainForm = false;

        do {
            Utils.clearConsole(20);
            consoleHeader();

            System.out.print(">> Please enter your username: ");
            String username = scanner.nextLine();

            System.out.print(">> Please enter your password: ");
            String password = scanner.nextLine();

            User retrievedUser = userService.findByUsername(username).get();

            if (retrievedUser != null && retrievedUser.checkUserPassword(password)) {
                loggedIn = true;
                AdminConsoleAdapter adminConsoleAdapter = new AdminConsoleAdapter(retrievedUser, userService, addressService, productService, orderService);

                switch (retrievedUser.getRole()) {
                    case "user" -> userMainForm();
                    case "admin" -> adminConsoleAdapter.adminDashboard();
                }
            } else {
                System.out.println("Incorrect username or password");
                System.out.println(">> Would you like to go back to the main form? (Y/N)");

                String choice = scanner.nextLine().trim().toUpperCase();
                goToMainForm = choice.equals("Y");

                if(goToMainForm) {
                    userMainForm();
                    return;
                }
            }
        } while (!loggedIn && !goToMainForm);
    }

    public List<User> showAllUsersTable() {
        List<User> userList = userService.getAllUsers();

        System.out.println("|-------------|--------------|------------------------|----------|----------------|---------------|----------------|");
        System.out.println("|   User ID   |   Username   |         Email          |   Role   |   First Name   |   Last Name   |   Birth Date   |");
        System.out.println("|-------------|--------------|------------------------|----------|----------------|---------------|----------------|");

        for (User user : userList) {
            System.out.printf("| %-12s| %-13s| %-23s| %-9s| %-15s| %-14s| %-15s|%n",
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getBirthDate());
        }

        System.out.println("|-------------|--------------|------------------------|----------|----------------|---------------|----------------|");
        return userList;
    }
}
