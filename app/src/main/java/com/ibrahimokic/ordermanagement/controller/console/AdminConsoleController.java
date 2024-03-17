package com.ibrahimokic.ordermanagement.controller.console;

import com.ibrahimokic.ordermanagement.controller.console.ui.ConsoleUserInterface;
import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.repository.AddressRepository;
import com.ibrahimokic.ordermanagement.repository.UserRepository;
import com.ibrahimokic.ordermanagement.service.impl.UserServiceImpl;
import com.ibrahimokic.ordermanagement.utils.Utils;

import java.time.LocalDate;

public class AdminConsoleController extends ConsoleUserInterface {
    private final User loggedUser;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public AdminConsoleController(User user, UserRepository userRepository, AddressRepository addressRepository) {
        this.loggedUser = user;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

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
            case 4 -> {
                UserConsoleController userConsoleController = new UserConsoleController(userRepository, addressRepository);
                userConsoleController.userMainForm();
            }
        }
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
        System.out.println("4.) Log out");
    }


    private void processAdminUserManagementChoice(int choice) {
        switch (choice) {
            case 1 -> adminUserListForm();
            case 2 -> createNewUserForm();
            case 3 -> deleteUserForm();
            case 4 -> {
                UserConsoleController userConsoleController = new UserConsoleController(userRepository, addressRepository);
                userConsoleController.userMainForm();
            }
        }
    }

    public void adminUserListForm() {
        Utils.clearConsole(20);
        consoleHeader();
        scanner.nextLine();

        UserConsoleController userConsoleController = new UserConsoleController(userRepository, addressRepository);
        userConsoleController.showAllUsersTable();

        Utils.returnBackToTheMainMenu(scanner);
        adminUserManagementOptions();
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
        String username = promptUserInput("username");
        String password = promptUserInput("password");
        String email = promptUserInput("email");

        User newAdminAccount = User.builder()
                .username(username)
                .password(password)
                .role("admin")
                .email(email)
                .build();

        saveUserAndReturnToMenu(newAdminAccount);
    }

    private void createUserAccountForm() {
        String username = promptUserInput("username");
        String password = promptUserInput("password");
        String email;

        do {
            email = promptUserInput("email");
            if (userRepository.findByEmail(email) != null) {
                System.out.println("User with this email already exists. Please try a different email address.");
            }
        } while (userRepository.findByEmail(email) != null);

        String name = promptUserInput("first name");
        String surname = promptUserInput("last name");
        LocalDate birthDate = LocalDate.parse(promptUserInput("birth date (yyyy-MM-dd)"));
        String streetName = promptUserInput("street address");
        String zipCode = promptUserInput("postal code");
        String city = promptUserInput("city");
        String country = promptUserInput("country");

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

        UserConsoleController userConsoleController = new UserConsoleController(userRepository, addressRepository);
        userConsoleController.showAllUsersTable();

        System.out.println(">> Please enter 'ID' of the user you want to delete.");

        Long userId = scanner.nextLong();
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

    private String promptUserInput(String fieldName) {
        System.out.print(">> Please enter the "+ fieldName +": ");
        return scanner.nextLine();
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
