package com.ibrahimokic.ordermanagement.controller.console;

import com.ibrahimokic.ordermanagement.controller.console.ui.ConsoleUserInterface;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.repository.UserRepository;
import com.ibrahimokic.ordermanagement.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConsoleController extends ConsoleUserInterface {
    private final UserRepository userRepository;

    @Autowired
    public UserConsoleController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void userMainForm() {
        Utils.clearConsole(20);
        consoleHeader();

        System.out.println("""
                [OM-APP]: Welcome! Take a moment to choose your next step:
                1.) Login
                2.) Register""");

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
            case 2 -> userRegisterForm();
        }

        scanner.close();
    }

    public void userLoginForm() {
        boolean loggedIn = false;
        boolean goToMainForm = false;

        do {
            Utils.clearConsole(20);
            consoleHeader();

            System.out.print(">> Please enter your e-mail: ");
            String email = scanner.nextLine();

            System.out.print(">> Please enter your password: ");
            String password = scanner.nextLine();

            User retrievedUser = userRepository.findByEmail(email);

            if (retrievedUser != null && retrievedUser.checkUserPassword(password)) {
                System.out.println("Successfully logged in");
                loggedIn = true;
            } else {
                System.out.println("Incorrect email or password");

                System.out.println("Would you like to go back to the main form? (Y/N)");
                String choice = scanner.nextLine().trim().toUpperCase();

                System.out.println("choice made: "+ choice);

                goToMainForm = choice.equals("Y");

                System.out.println(loggedIn);
                System.out.println(goToMainForm);
            }
        } while (!loggedIn && !goToMainForm);
    }

    public static void userRegisterForm() {
        Utils.clearConsole(20);
        consoleHeader(); 
    }
}
