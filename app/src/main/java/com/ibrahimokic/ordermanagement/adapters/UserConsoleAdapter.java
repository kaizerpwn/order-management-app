package com.ibrahimokic.ordermanagement.adapters;

import com.ibrahimokic.ordermanagement.adapters.ui.ConsoleUserInterface;
import com.ibrahimokic.ordermanagement.domain.dto.AddressDto;
import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import com.ibrahimokic.ordermanagement.domain.dto.OrderItemDto;
import com.ibrahimokic.ordermanagement.domain.entity.Order;
import com.ibrahimokic.ordermanagement.domain.entity.OrderItem;
import com.ibrahimokic.ordermanagement.domain.entity.Product;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.mapper.impl.OrderItemMapperImpl;
import com.ibrahimokic.ordermanagement.service.AddressService;
import com.ibrahimokic.ordermanagement.service.OrderService;
import com.ibrahimokic.ordermanagement.service.ProductService;
import com.ibrahimokic.ordermanagement.service.UserService;
import com.ibrahimokic.ordermanagement.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.*;

import static com.ibrahimokic.ordermanagement.adapters.OrderConsoleAdapter.getOrders;

@RequiredArgsConstructor
public class UserConsoleAdapter extends ConsoleUserInterface {
    private final UserService userService;
    private final AddressService addressService;
    private final ProductService productService;
    private final OrderService orderService;
    private User loggedUser;
    private final List<Product> currentOrder = new ArrayList<>();
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
        boolean goToMainForm;

        do {
            Utils.clearConsole(20);
            consoleHeader();

            System.out.print(">> Please enter your username: ");
            String username = scanner.nextLine();

            System.out.print(">> Please enter your password: ");
            String password = scanner.nextLine();

            User retrievedUser = userService.findByUsername(username).get();

            if (retrievedUser.checkUserPassword(password)) {
                loggedIn = true;
                loggedUser = retrievedUser;
                AdminConsoleAdapter adminConsoleAdapter = new AdminConsoleAdapter(retrievedUser, userService, addressService, productService, orderService);

                switch (retrievedUser.getRole()) {
                    case "user" -> showUserOptions();
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
        } while (!loggedIn);
    }

    public void showAllUsersTable() {
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
    }

    public void displayUserMenu() {
        Utils.clearConsole(20);
        consoleHeader();

        System.out.println("[OM-APP]: Welcome, " + loggedUser.getFirstName() + "! Take a moment to choose your next step:");
        System.out.println("1.) List of all orders");
        System.out.println("2.) Order new products");
        System.out.println("3.) Log out");
    }

    public void showUserOptions() {
        Utils.clearConsole(20);
        consoleHeader();

        displayUserMenu();

        int choice = Utils.getValidInput(scanner,  3);
        processUserMenuChoice(choice);
    }

    private void processUserMenuChoice(int choice) {
        switch (choice) {
            case 1: {
                showAllUsersOrders();
                Utils.returnBackToTheMainMenu(scanner);
                showUserOptions();
            }
            case 2: {
                createNewOrder();
                Utils.returnBackToTheMainMenu(scanner);
                showUserOptions();
            }
            case 3:
            {
                loggedUser = null;
                userMainForm();
                break;
            }
        }
    }

    private void createNewOrder() {
        List<Product> productList = productService.getAllAvailableProducts();
        ProductConsoleAdapter.showProductsInTable(productList);

        System.out.println(">> To add a product to your order, please type 'ID of the product and quantity', example: 5 10.");
        System.out.println(">> If you want to remove a product from the cart, type 'REMOVE and ID of the product'..");
        System.out.println(">> You can also view all selected products, simply type 'VIEW CART'..");
        System.out.println(">> If you want to finish your order, type 'FINISH ORDER'..");
        System.out.println(">> Also, you can cancel your order by typing 'CANCEL ORDER'..");

        boolean finishedOrder = false;

        while (!finishedOrder) {
            String usersInput = scanner.nextLine().trim();

            switch (usersInput.toUpperCase()) {
                case "CANCEL ORDER" -> {
                    currentOrder.clear();
                    finishedOrder = true;
                }
                case "VIEW CART" -> {
                    System.out.println("|---------------------------------------------------------------------------------------------------------------------|");
                    System.out.println("|                                       [ CURRENTLY ADDED PRODUCTS TO THE CART ]                                      |");
                    System.out.println("|---------------------------------------------------------------------------------------------------------------------|");
                    ProductConsoleAdapter.showProductsInTable(currentOrder);
                }
                case "FINISH ORDER" -> {
                    finishOrder();
                    finishedOrder = true;
                }
                default -> {
                    if (usersInput.toUpperCase().startsWith("REMOVE")) {
                        String[] inputParts = usersInput.split(" ");
                        if (inputParts.length < 2) {
                            System.out.println("ERROR: Invalid input format. Please enter the product ID after 'REMOVE'.");
                        } else {
                            String productIdToRemove = inputParts[1];
                            removeProductFromCart(productIdToRemove);
                        }
                    } else {
                        processUserInput(usersInput);
                    }
                }
            }
        }
    }

    private void removeProductFromCart(String productIdToRemove) {
        try {
            long productId = Long.parseLong(productIdToRemove);
            boolean found = false;
            for (Product product : currentOrder) {
                if (product.getProductId() == productId) {
                    currentOrder.remove(product);
                    System.out.println("Product with ID '" + productIdToRemove + "' removed from the cart.");
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("ERROR: Product with ID '" + productIdToRemove + "' not found in the cart.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Invalid product ID format.");
        }
    }

    private void finishOrder() {
        try {
            if(currentOrder.size() == 0) {
                System.out.println("ERROR: Your cart is currently empty, so order is not created.");
                return;
            }
            Long unavailableProduct = null;
            OrderItemMapperImpl orderItemMapper = new OrderItemMapperImpl(new ModelMapper());
            OrderDto newOrder = new OrderDto();
            AddressDto deliveryAddress = AddressDto.builder()
                    .city(loggedUser.getAddress().getCity())
                    .street(loggedUser.getAddress().getStreet())
                    .country(loggedUser.getAddress().getCountry())
                    .zip(loggedUser.getAddress().getZip())
                    .build();
            AddressDto sourceAddress = AddressDto.builder()
                    .zip("71000")
                    .country("Bosnia and Herzegovina")
                    .street("Džemala Bijedića Bb")
                    .city("Sarajevo")
                    .build();

            List<OrderItemDto> orderItemsList = new ArrayList<>();
            for (Product product : currentOrder) {
                Optional<Product> retrievedProduct = productService.getProductById(product.getProductId());

                if (retrievedProduct.isPresent()) {
                    if (retrievedProduct.get().getAvailableQuantity() >= product.getAvailableQuantity()) {
                        OrderItemDto orderItem = new OrderItemDto();
                        orderItem.setProductId(product.getProductId());
                        orderItem.setItemPrice(product.getPrice());
                        orderItem.setQuantity(product.getAvailableQuantity());

                        orderItemsList.add(orderItem);
                    } else {
                        unavailableProduct = retrievedProduct.get().getProductId();
                    }
                }
            }

            if (unavailableProduct != null) {
                System.out.println("ERROR: Product with ID: '" + unavailableProduct + "' does not have the quantity you ordered.");
                System.out.println("ERROR: Please use 'REMOVE " + unavailableProduct + "' to remove that product from the cart to continue the order.");
            } else {
                List<OrderItem> mappedOrderItems = orderItemMapper.mapListToEntityList(orderItemsList);

                newOrder.setUserId(loggedUser.getUserId());
                newOrder.setDeliveryAddress(deliveryAddress);
                newOrder.setSourceAddress(sourceAddress);
                newOrder.setOrderDate(LocalDate.now());
                newOrder.setOrderItems(orderItemsList);
                newOrder.setTotalAmount(Utils.calculateTotalProductsPriceAmount(mappedOrderItems));

                orderService.createNewOrder(newOrder);
                System.out.println("|---------------------------------------------------------------------------------------------------------------------|");
                System.out.println("|                                          [ NEW ORDER CREATED SUCCESSFULLY ]                                         |");
                System.out.println("|---------------------------------------------------------------------------------------------------------------------|");
                ProductConsoleAdapter.showProductsInTable(currentOrder);
            }
        } catch (Exception e) {
            System.out.println("ERROR: An error occurred while creating an order.");
        }
    }

    private void processUserInput(String userInput) {
        if (userInput == null || userInput.isEmpty()) {
            return;
        }

        String[] inputParts = userInput.split(" ");
        if (inputParts.length < 2) {
            return;
        }

        String productId = inputParts[0];
        int quantity;
        try {
            quantity = Integer.parseInt(inputParts[1]);
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Invalid quantity format. Please enter a valid number for the quantity.");
            return;
        }

        if(quantity == 0) {
            System.out.println("ERROR: Quantity can't be zero, please choose different value.");
            return;
        }

        try {
            Optional<Product> product = productService.getProductById(Long.parseLong(productId));
            if (product.isEmpty()) {
                System.out.println("ERROR: Product with ID '" + productId + "' does not exist.");
                return;
            }

            if (Utils.checkProductQuantity(product.get(), quantity)) {
                System.out.println("ERROR: Quantity available for that product is '" + product.get().getAvailableQuantity() + "', not '" + quantity + "'.");
                return;
            }

            if (Utils.checkProductAvailability(product.get())) {
                System.out.println("ERROR: That product is not currently available, it is available from the date " + product.get().getAvailableFrom() + " to "+ product.get().getAvailableUntil() + ".");
                return;
            }

            boolean found = false;
            for (Product p : currentOrder) {
                if (p.getProductId().equals(product.get().getProductId())) {
                    found = true;
                    int updatedQuantity = p.getAvailableQuantity() + quantity;
                    if (updatedQuantity <= product.get().getAvailableQuantity()) {
                        p.setAvailableQuantity(updatedQuantity);
                        System.out.println("ORDER-LIST: Quantity of Product with ID '" + productId + "' updated to " + updatedQuantity);
                    } else {
                        System.out.println("ERROR: Quantity available for that product is '" + product.get().getAvailableQuantity() + "', not '" + updatedQuantity + "'.");
                    }
                    break;
                }
            }

            if (!found) {
                product.get().setAvailableQuantity(quantity);
                currentOrder.add(product.get());
                System.out.println("ORDER-LIST: " + quantity + "x Product with ID '" + productId + "' successfully added to list.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Invalid input. Please provide a valid input.");
        }
    }

    public void showAllUsersOrders() {
        List<Order> orderList = orderService.getAllUsersOrders(loggedUser);

        getOrders(orderList);
    }
}
