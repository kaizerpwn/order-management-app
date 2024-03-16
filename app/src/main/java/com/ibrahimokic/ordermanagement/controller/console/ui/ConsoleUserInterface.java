package com.ibrahimokic.ordermanagement.controller.console.ui;

import java.util.Scanner;

public abstract class ConsoleUserInterface {
    public static final Scanner scanner = new Scanner(System.in);
    public static void consoleHeader() {
        System.out.println("  ____          _             __  __                                                   _   ");
        System.out.println(" / __ \\        | |           |  \\/  |                                                 | |  ");
        System.out.println("| |  | |_ __ __| | ___ _ __  | \\  / | __ _ _ __   __ _  __ _  ___ _ __ ___   ___ _ __ | |_ ");
        System.out.println("| |  | | '__/ _` |/ _ \\ '__| | |\\/| |/ _` | '_ \\ / _` |/ _` |/ _ \\ '_ ` _ \\ / _ \\ '_ \\| __|");
        System.out.println("| |__| | | | (_| |  __/ |    | |  | | (_| | | | | (_| | (_| |  __/ | | | | |  __/ | | | |_ ");
        System.out.println(" \\____/|_|  \\__,_|\\___|_|    |_|  |_|\\__,_|_| |_|\\__,_|\\__, |\\___|_| |_| |_|\\___|_| |_|\\__|");
        System.out.println("                                                         __/ |                              ");
        System.out.println("                                                        |___/                               ");
        System.out.println("                                                        Author: Ibrahim Okic");
    }
}
