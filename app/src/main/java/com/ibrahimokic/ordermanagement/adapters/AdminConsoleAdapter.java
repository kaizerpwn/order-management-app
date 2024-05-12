package com.ibrahimokic.ordermanagement.adapters;

import com.ibrahimokic.ordermanagement.adapters.ui.ConsoleUserInterface;
import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.domain.entity.User;  
import com.ibrahimokic.ordermanagement.service.AddressService;
import com.ibrahimokic.ordermanagement.service.OrderService;
import com.ibrahimokic.ordermanagement.service.ProductService;
import com.ibrahimokic.ordermanagement.service.UserService;
import com.ibrahimokic.ordermanagement.utils.Utils;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class AdminConsoleAdapter extends ConsoleUserInterface {
    private final User loggedUser;
    private final UserService userService;
    private final AddressService addressService;
    private final ProductService productService;
    private final OrderService orderService;

    public void adminDashboard() {
        Utils.clearConsole(20);
        consoleHeader();

        displayAdminDashboardMenu();

        int choice = Utils.getValidInput(scanner,  4);
        processAdminDashboardChoice(choice);
    }

    private void displayAdminDashboardMenu() {
        System.out.println("[OM-APP]: Welcome, " + loggedUser.getFirstName() + "! Take a moment to choose your next step:");
        System.out.println("1.) Manage users");
        System.out.println("2.) Manage products");
        System.out.println("3.) Manage orders");
        System.out.println("4.) Log out");
    }

    private void processAdminDashboardChoice(int choice) {
        switch (choice) {
            case 1 -> adminUserManagementOptions();
            case 2 -> adminProductManagementOptions();
            case 3 -> adminOrderManagementOptions();
            case 4 -> {
                UserConsoleAdapter userConsoleAdapter = new UserConsoleAdapter(userService, addressService, productService, orderService);
                userConsoleAdapter.userMainForm();
            }
        }
    }

    public void adminOrderManagementOptions() {
        Utils.clearConsole(20);
        consoleHeader();

        OrderConsoleAdapter orderConsoleAdapter = new OrderConsoleAdapter(loggedUser, orderService);
        orderConsoleAdapter.displayAdminOrderManagementMenu();

        int choice = Utils.getValidInput(scanner,  3);
        processAdminOrderManagementChoice(choice);
    }

    public void adminProductManagementOptions() {
        Utils.clearConsole(20);
        consoleHeader();

        ProductConsoleAdapter productConsoleAdapter = new ProductConsoleAdapter(loggedUser, productService);
        productConsoleAdapter.displayAdminProductManagementMenu();

        int choice = Utils.getValidInput(scanner,  4);
        processAdminProductManagementChoice(choice);
    }

    public void adminUserManagementOptions() {
        Utils.clearConsole(20);

        consoleHeader();
        displayAdminUserManagementMenu();

        int choice = Utils.getValidInput(scanner,  4);
        processAdminUserManagementChoice(choice);
    }

    private void displayAdminUserManagementMenu() {
        System.out.println("[OM-APP]: Welcome, " + loggedUser.getFirstName() + "! Take a moment to choose your next step:");
        System.out.println("1.) List of the users");
        System.out.println("2.) Create new user");
        System.out.println("3.) Delete a user");
        System.out.println("4.) Return to the main menu");
    }

    private void processAdminUserManagementChoice(int choice) {
        switch (choice) {
            case 1 -> adminUserListForm();
            case 2 -> createNewUserForm();
            case 3 -> deleteUserForm();
            case 4 -> adminDashboard();
        }
    }

    private void processAdminProductManagementChoice(int choice) {
        switch (choice) {
            case 1 -> adminProductListForm();
            case 2 -> adminProductCreationForm();
            case 3 -> adminProductDeletionForm();
            case 4 -> adminDashboard();
        }
    }

    private void processAdminOrderManagementChoice(int choice) {
        switch (choice) {
            case 1 -> adminOrderListForm();
            case 2 -> adminOrderExportForm();
            case 3 -> adminDashboard();
        }
    }

    public void adminUserListForm() {
        Utils.clearConsole(20);
        consoleHeader();

        UserConsoleAdapter userConsoleAdapter = new UserConsoleAdapter(userService, addressService, productService, orderService);
        userConsoleAdapter.showAllUsersTable();

        Utils.returnBackToTheMainMenu(scanner);
        adminUserManagementOptions();
    }

    public void adminProductListForm() {
        Utils.clearConsole(20);
        consoleHeader();

        ProductConsoleAdapter productConsoleAdapter = new ProductConsoleAdapter(loggedUser, productService);
        productConsoleAdapter.showAllProductsTable();

        Utils.returnBackToTheMainMenu(scanner);
        adminProductManagementOptions();
    }

    public void adminOrderListForm() {
        Utils.clearConsole(20);
        consoleHeader();

        OrderConsoleAdapter orderConsoleAdapter = new OrderConsoleAdapter(loggedUser, orderService);
        orderConsoleAdapter.showAllOrdersList();

        Utils.returnBackToTheMainMenu(scanner);
        adminOrderManagementOptions();
    }

    public void adminOrderExportForm() {
        Utils.clearConsole(20);
        consoleHeader();
        scanner.nextLine();

        OrderConsoleAdapter orderConsoleAdapter = new OrderConsoleAdapter(loggedUser, orderService);
        orderConsoleAdapter.exportOrdersAsExcelFile();

        Utils.returnBackToTheMainMenu(scanner);
        adminOrderManagementOptions();
    }

    public void adminProductCreationForm() {
        Utils.clearConsole(20);
        consoleHeader();
        scanner.nextLine();

        ProductConsoleAdapter productConsoleAdapter = new ProductConsoleAdapter(loggedUser, productService);
        productConsoleAdapter.createNewProduct();

        adminProductManagementOptions();
    }

    public void createNewUserForm() {
        Utils.clearConsole(20);
        consoleHeader();

        displayUserCreationOptions();

        String role = scanner.next().toLowerCase();
        scanner.nextLine();

        switch (role) {
            case "admin" -> createAdminAccountForm();
            case "user" -> createUserAccountForm();
            case "back" -> adminUserManagementOptions();
            default -> System.out.println("Invalid role specified. Please choose 'user' or 'admin'.");
        }
    }

    private void displayUserCreationOptions() {
        System.out.println("OPTIONS: user | admin");
        System.out.println("OPTIONS: back (return back to the main menu)");
        System.out.print(">> Please insert which type of account you want to create: ");
    }

    private void createAdminAccountForm() {
        String username = Utils.promptUserInput(scanner,"username");
        String password = Utils.promptUserInput(scanner,"password");
        String email = Utils.promptUserInput(scanner,"email");

        User newAdminAccount = User.builder()
                .username(username)
                .password(password)
                .role("admin")
                .email(email)
                .build();

        saveUserAndReturnToMenu(newAdminAccount);
    }

    private void createUserAccountForm() {
        try {
            String username = Utils.promptUserInput(scanner,"username");
            String password = Utils.promptUserInput(scanner,"password");
            String email;

            do {
                email = Utils.promptUserInput(scanner,"email");
                if (userService.findByEmail(email).isPresent()) {
                    System.out.println("User with this email already exists. Please try a different email address.");
                }
            } while (userService.findByEmail(email).isPresent());

            String name = Utils.promptUserInput(scanner,"first name");
            String surname = Utils.promptUserInput(scanner,"last name");
            LocalDate birthDate = LocalDate.parse(Utils.promptUserInput(scanner,"birth date (yyyy-MM-dd)"));
            String streetName = Utils.promptUserInput(scanner,"street address");
            String zipCode = Utils.promptUserInput(scanner,"postal code");
            String city = Utils.promptUserInput(scanner,"city");
            String country = Utils.promptUserInput(scanner,"country");

            Address newAddress = Address.builder()
                    .country(country)
                    .city(city)
                    .zip(zipCode)
                    .street(streetName)
                    .build();

            User newUserAccount = User.builder()
                    .username(username)
                    .password(password)
                    .role("user")
                    .email(email)
                    .firstName(name)
                    .lastName(surname)
                    .birthDate(birthDate)
                    .address(newAddress)
                    .build();

            saveUserAndReturnToMenu(newUserAccount);
        }
        catch (ConstraintViolationException e) {
            e.getConstraintViolations().forEach(violation ->
                    System.out.println(violation.getMessage())
            );
            Utils.returnBackToTheMainMenu(scanner);
            adminUserManagementOptions();
        }
        catch (Exception e) {
            System.out.println("ERROR: "+ e.getMessage());
            Utils.returnBackToTheMainMenu(scanner);
            adminUserManagementOptions();
        }
    }

    private void deleteUserForm() {
        Utils.clearConsole(20);
        consoleHeader();
        scanner.nextLine();

        UserConsoleAdapter userConsoleAdapter = new UserConsoleAdapter(userService, addressService, productService, orderService);
        userConsoleAdapter.showAllUsersTable();

        System.out.println(">> Please enter 'ID' of the user you want to delete.");
        System.out.println(">> Press 'ENTER ' if you want to go back to the main menu.");

        String userIdInput = scanner.nextLine();

        if (userIdInput.isEmpty()) {
            adminUserManagementOptions();
            return;
        }

        Long userId;

        try {
            userId = Long.parseLong(userIdInput);

            if (userService.findById(userId).isPresent()) {
                userService.deleteUser(userId);
                System.out.println("Successfully deleted user with ID: " + userId);
            } else {
                System.out.println("User with that ID does not exist.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid user ID.");
            return;
        }

        Utils.returnBackToTheMainMenu(scanner);
        adminUserManagementOptions();
    }

    private void adminProductDeletionForm() {
        ProductConsoleAdapter productConsoleAdapter = new ProductConsoleAdapter(loggedUser, productService);

        productConsoleAdapter.deleteProductForm();
        adminProductManagementOptions();
    }

    private void saveUserAndReturnToMenu(User user) {
        try {
            userService.createUser(user);
            System.out.println("OM-APP: New account '" + user.getFirstName() + " " + user.getLastName() + "' with role '"+ user.getRole() +"' has been successfully created.");
        }
        catch (ConstraintViolationException e) {
            e.getConstraintViolations().forEach(violation ->
                    System.out.println(violation.getMessage())
            );
        }
        catch (Exception e) {
            System.out.println("ERROR: An error occurred while creating an user, please check your inputs.");
        }

        Utils.returnBackToTheMainMenu(scanner);
        adminUserManagementOptions();
    }
}
