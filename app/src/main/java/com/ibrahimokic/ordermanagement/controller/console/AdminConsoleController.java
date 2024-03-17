package com.ibrahimokic.ordermanagement.controller.console;

import com.ibrahimokic.ordermanagement.controller.console.ui.ConsoleUserInterface;
import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.repository.AddressRepository;
import com.ibrahimokic.ordermanagement.repository.UserRepository;
import com.ibrahimokic.ordermanagement.service.impl.UserServiceImpl;
import com.ibrahimokic.ordermanagement.utils.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminConsoleController extends ConsoleUserInterface {
    private final User loggedUser;

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    public AdminConsoleController(User user, UserRepository userRepository, AddressRepository addressRepository) {
        this.loggedUser = user;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    public void adminDashboard(){
        Utils.clearConsole(20);
        consoleHeader();

        System.out.println(String.format("""
                [OM-APP]: Welcome, %s! Take a moment to choose your next step:
                1.) Manage users
                2.) Manage products
                3.) Manage orders
                4.) Log out""", loggedUser.getFirstName()));

        int choice;
        do {
            System.out.print(">> Please enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please use options 1 - 4.");
                System.out.print("Enter your choice (1 - 4): ");
                scanner.next();
            }

            choice = scanner.nextInt();
            if (choice < 1 || choice > 4)
                System.out.println("Invalid choice. Please enter 1 - 4.");
        } while (choice < 1 || choice > 4);

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

        System.out.println(String.format("""
                [OM-APP]: Welcome, %s! Take a moment to choose your next step:
                1.) List of the users
                2.) Create new user
                3.) Delete a user
                4.) Log out""", loggedUser.getFirstName()));

        int choice;
        do {
            System.out.print(">> Please enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please use options 1 - 4.");
                System.out.print("Enter your choice (1 - 4): ");
                scanner.next();
            }

            choice = scanner.nextInt();
            if (choice < 1 || choice > 4)
                System.out.println("Invalid choice. Please enter 1 - 4.");
        } while (choice < 1 || choice > 4);

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

        System.out.println("OPTIONS: user | admin");
        System.out.println("OPTIONS: back (return back to the main menu)");
        System.out.print(">> Please insert which type of account you want to create: ");

        String role = scanner.next();
        scanner.nextLine();

        switch (role.toLowerCase()) {
            case "admin" -> createAdminAccountForm();
            case "user" -> createUserAccountForm();
            case "back" -> adminUserManagementOptions();
            default -> System.out.println("Invalid role specified. Please choose 'user' or 'admin'.");
        }
    }

    private void createAdminAccountForm() {
        System.out.print(">> Please enter the username for the admin: ");
        String username = scanner.nextLine();

        System.out.print(">> Please enter the password for the admin: ");
        String password = scanner.nextLine();

        System.out.print(">> Please enter the email address for the admin: ");
        String email = scanner.nextLine();

        User newAdminAccount = User.builder()
                .username(username)
                .password(password)
                .role("admin")
                .email(email)
                .build();

        UserServiceImpl userService = new UserServiceImpl(userRepository);
        userService.createUser(newAdminAccount);

        Utils.returnBackToTheMainMenu(scanner);
        adminUserManagementOptions();
    }

    private void createUserAccountForm() {
        System.out.print(">> Please enter the username for the user: ");
        String username = scanner.nextLine();

        System.out.print(">> Please enter the password for the user: ");
        String password = scanner.nextLine();

        String email;
        boolean userExists;

        do {
            System.out.print(">> Please enter the email address for the user: ");
            email = scanner.nextLine();

            if (userRepository.findByEmail(email) != null) {
                System.out.println("User with this email already exists. Please try different email address.");
                userExists = true;
            } else {
                userExists = false;
            }
        } while (userExists);

        System.out.print(">> Please enter the first name of the user: ");
        String name = scanner.nextLine();

        System.out.print(">> Please enter the last name of the user: ");
        String surname = scanner.nextLine();

        System.out.print(">> Please enter the birth date of the user (yyyy-MM-dd): ");
        LocalDate birthDate = LocalDate.parse(scanner.nextLine());

        System.out.print(">> Please enter the street address of the user: ");
        String streetName = scanner.nextLine();

        System.out.print(">> Please enter the postal code of the user: ");
        String zipCode = scanner.nextLine();

        System.out.print(">> Please enter the city of the user: ");
        String city = scanner.nextLine();

        System.out.print(">> Please enter the country of the user: ");
        String country = scanner.nextLine();

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

        UserServiceImpl userService = new UserServiceImpl(userRepository);
        userService.createUser(newUserAccount);

        Utils.returnBackToTheMainMenu(scanner);
        adminUserManagementOptions();
    }

    public void deleteUserForm() {
        Utils.clearConsole(20);
        consoleHeader();
        scanner.nextLine();

        UserConsoleController userConsoleController = new UserConsoleController(userRepository, addressRepository);
        userConsoleController.showAllUsersTable();

        System.out.println(">> Please enter 'ID' of the user you want to delete.");
        Long userId = scanner.nextLong();

        UserServiceImpl userService = new UserServiceImpl(userRepository);

        if(userRepository.findById(userId) != null) {
            userService.deleteUser(userId);
            System.out.println("Successfully deleted user with ID: "+ userId);
        } else {
            System.out.println("User with that ID does not exist.");
        }

        Utils.returnBackToTheMainMenu(scanner);
        adminUserManagementOptions();
    }
}
