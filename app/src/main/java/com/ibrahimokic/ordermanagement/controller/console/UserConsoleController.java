package com.ibrahimokic.ordermanagement.controller.console;

import com.ibrahimokic.ordermanagement.controller.console.ui.ConsoleUserInterface;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.repository.AddressRepository;
import com.ibrahimokic.ordermanagement.repository.ProductRepository;
import com.ibrahimokic.ordermanagement.repository.UserRepository;
import com.ibrahimokic.ordermanagement.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserConsoleController extends ConsoleUserInterface {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    @Autowired
    public UserConsoleController(UserRepository userRepository,
                                 AddressRepository addressRepository,
                                 ProductRepository productRepository){
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
    }

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

            User retrievedUser = userRepository.findByUsername(username);

            if (retrievedUser != null && retrievedUser.checkUserPassword(password)) {
                loggedIn = true;
                AdminConsoleController adminConsoleController = new AdminConsoleController(retrievedUser, userRepository, addressRepository, productRepository);

                switch (retrievedUser.getRole()) {
                    case "user" -> userMainForm();
                    case "admin" -> adminConsoleController.adminDashboard();
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
        List<User> userList = userRepository.findAll();

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
