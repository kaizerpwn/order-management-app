package com.ibrahimokic.ordermanagement.controller.console;

import com.ibrahimokic.ordermanagement.controller.console.ui.ConsoleUserInterface;
import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.repository.AddressRepository;
import com.ibrahimokic.ordermanagement.repository.OrderRepository;
import com.ibrahimokic.ordermanagement.repository.ProductRepository;
import com.ibrahimokic.ordermanagement.repository.UserRepository;
import com.ibrahimokic.ordermanagement.service.impl.UserServiceImpl;
import com.ibrahimokic.ordermanagement.utils.Utils;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class AdminConsoleController extends ConsoleUserInterface {
    private final User loggedUser;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public void adminDashboard() {
        Utils.clearConsole(20);
        consoleHeader();

        displayAdminDashboardMenu();

        int choice = getValidInput(  4);
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
                UserConsoleController userConsoleController = new UserConsoleController(userRepository, addressRepository, productRepository, orderRepository);
                userConsoleController.userMainForm();
            }
        }
    }

    public void adminOrderManagementOptions() {
        Utils.clearConsole(20);
        consoleHeader();

        OrderConsoleController orderConsoleController = new OrderConsoleController(loggedUser, orderRepository);
        orderConsoleController.displayAdminOrderManagementMenu();

        int choice = getValidInput(  3);
        processAdminOrderManagementChoice(choice);
    }

    public void adminProductManagementOptions() {
        Utils.clearConsole(20);
        consoleHeader();

        ProductConsoleController productConsoleController = new ProductConsoleController(loggedUser, productRepository);
        productConsoleController.displayAdminProductManagementMenu();

        int choice = getValidInput(  4);
        processAdminProductManagementChoice(choice);
    }

    public void adminUserManagementOptions() {
        Utils.clearConsole(20);

        consoleHeader();
        displayAdminUserManagementMenu();

        int choice = getValidInput(  4);
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

        UserConsoleController userConsoleController = new UserConsoleController(userRepository, addressRepository, productRepository, orderRepository);
        userConsoleController.showAllUsersTable();

        Utils.returnBackToTheMainMenu(scanner);
        adminUserManagementOptions();
    }

    public void adminProductListForm() {
        Utils.clearConsole(20);
        consoleHeader();

        ProductConsoleController productConsoleController = new ProductConsoleController(loggedUser, productRepository);
        productConsoleController.showAllProductsTable();

        Utils.returnBackToTheMainMenu(scanner);
        adminProductManagementOptions();
    }

    public void adminOrderListForm() {
        Utils.clearConsole(20);
        consoleHeader();

        OrderConsoleController orderConsoleController = new OrderConsoleController(loggedUser, orderRepository);
        orderConsoleController.showAllOrdersList();

        Utils.returnBackToTheMainMenu(scanner);
        adminOrderManagementOptions();
    }

    public void adminOrderExportForm() {
        Utils.clearConsole(20);
        consoleHeader();
        scanner.nextLine();

        OrderConsoleController orderConsoleController = new OrderConsoleController(loggedUser, orderRepository);
        orderConsoleController.exportOrdersAsExcelFile();

        Utils.returnBackToTheMainMenu(scanner);
        adminOrderManagementOptions();
    }

    public void adminProductCreationForm() {
        Utils.clearConsole(20);
        consoleHeader();
        scanner.nextLine();

        ProductConsoleController productConsoleController = new ProductConsoleController(loggedUser, productRepository);
        productConsoleController.createNewProduct();

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
        String username = Utils.promptUserInput(scanner,"username");
        String password = Utils.promptUserInput(scanner,"password");
        String email;

        do {
            email = Utils.promptUserInput(scanner,"email");
            if (userRepository.findByEmail(email) != null) {
                System.out.println("User with this email already exists. Please try a different email address.");
            }
        } while (userRepository.findByEmail(email) != null);

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

    private void deleteUserForm() {
        Utils.clearConsole(20);
        consoleHeader();
        scanner.nextLine();

        UserConsoleController userConsoleController = new UserConsoleController(userRepository, addressRepository, productRepository, orderRepository);
        userConsoleController.showAllUsersTable();

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
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid user ID.");
            return;
        }

        UserServiceImpl userService = new UserServiceImpl(userRepository);

        if (userRepository.findById(userId).isPresent()) {
            userService.deleteUser(userId);
            System.out.println("Successfully deleted user with ID: " + userId);
        } else {
            System.out.println("User with that ID does not exist.");
        }

        Utils.returnBackToTheMainMenu(scanner);
        adminUserManagementOptions();
    }

    private void adminProductDeletionForm() {
        ProductConsoleController productConsoleController = new ProductConsoleController(loggedUser, productRepository);

        productConsoleController.deleteProductForm();
        adminProductManagementOptions();
    }

    private void saveUserAndReturnToMenu(User user) {
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        userService.createUser(user);

        Utils.returnBackToTheMainMenu(scanner);
        adminUserManagementOptions();
    }

    private int getValidInput(int max) {
        final int MIN_CHOICE = 1;
        int choice;
        do {
            System.out.print(">> Please enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please use options " + MIN_CHOICE + " - " + max + ".");
                System.out.print("Enter your choice (" + MIN_CHOICE + " - " + max + "): ");
                scanner.next();
            }
            choice = scanner.nextInt();
            if (choice < MIN_CHOICE || choice > max)
                System.out.println("Invalid choice. Please enter " + MIN_CHOICE + " - " + max + ".");
        } while (choice < MIN_CHOICE || choice > max);
        return choice;
    }
}
