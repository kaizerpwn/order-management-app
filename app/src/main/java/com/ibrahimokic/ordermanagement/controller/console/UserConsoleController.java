package com.ibrahimokic.ordermanagement.controller.console;

import com.ibrahimokic.ordermanagement.controller.console.ui.ConsoleUserInterface;
import com.ibrahimokic.ordermanagement.utils.Utils;

public class UserConsoleController extends ConsoleUserInterface {
    public static void userMainForm() {
        Utils.clearConsole(20);
        consoleHeader();

        System.out.println(">> Welcome! Take a moment to choose your next step:\n" +
                "1.) Login\n" +
                "2.) Register");

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
            case 1:
                userLoginForm();

            case 2:
                userRegisterForm();
        }

        scanner.close();
    }

    public static void userLoginForm() {

    }

    public static void userRegisterForm() {

    }
}
