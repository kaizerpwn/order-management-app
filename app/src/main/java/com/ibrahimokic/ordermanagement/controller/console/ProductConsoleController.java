package com.ibrahimokic.ordermanagement.controller.console;

import com.ibrahimokic.ordermanagement.controller.console.ui.ConsoleUserInterface;
import com.ibrahimokic.ordermanagement.domain.entity.Product;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.repository.ProductRepository;
import com.ibrahimokic.ordermanagement.service.impl.ProductServiceImpl;
import com.ibrahimokic.ordermanagement.utils.Utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ProductConsoleController extends ConsoleUserInterface {
    private final User loggedUser;
    private final ProductRepository productRepository;

    public ProductConsoleController(User loggedUser, ProductRepository productRepository) {
        this.loggedUser = loggedUser;
        this.productRepository = productRepository;
    }

    public void displayAdminProductManagementMenu() {
        Utils.clearConsole(20);
        consoleHeader();

        System.out.println("[OM-APP]: Welcome, " + loggedUser.getFirstName() + "! Take a moment to choose your next step:");
        System.out.println("1.) List of the products");
        System.out.println("2.) Create new product");
        System.out.println("3.) Delete a product");
        System.out.println("4.) Return to the main manu");
    }

    public List<Product> showAllProductsTable() {
        List<Product> productList = productRepository.findAll();

        System.out.println("|-------------|---------------------|---------------|---------------------|---------------------|---------------------|");
        System.out.println("|  Product ID |     Product Name    |     Price     |    Available From   |    Available Until  |  Available Quantity |");
        System.out.println("|-------------|---------------------|---------------|---------------------|---------------------|---------------------|");

        for (Product product : productList) {
            System.out.printf("| %-12s| %-20s| $%-13s| %-20s| %-20s| %-20s|%n",
                    product.getProductId(),
                    product.getProductName(),
                    product.getPrice(),
                    product.getAvailableFrom(),
                    product.getAvailableUntil(),
                    product.getAvailableQuantity());
        }

        System.out.println("|-------------|---------------------|---------------|---------------------|---------------------|---------------------|");
        return productList;
    }

    public void createNewProduct() {
        System.out.println(">> Create a new product");
        System.out.println(">> To cancel and go back to the main menu just press 'ENTER'");

        String productName = Utils.promptUserInput(scanner,"product name");

        if(productName.length() == 0) {
            return;
        }

        BigDecimal price = new BigDecimal(Utils.promptUserInput(scanner,"price"));
        LocalDate availableFrom = LocalDate.parse(Utils.promptUserInput(scanner,"available from (yyyy-MM-dd)"));
        LocalDate availableUntil = LocalDate.parse(Utils.promptUserInput(scanner,"available until (yyyy-MM-dd)"));
        int availableQuantity = Integer.parseInt(Utils.promptUserInput(scanner,"available quantity"));

        Product newProduct = new Product();
        newProduct.setProductName(productName);
        newProduct.setPrice(price);
        newProduct.setAvailableFrom(availableFrom);
        newProduct.setAvailableUntil(availableUntil);
        newProduct.setAvailableQuantity(availableQuantity);

        productRepository.save(newProduct);
        System.out.println("Product '"+productName+"' successfully created.");

        Utils.returnBackToTheMainMenu(scanner);
    }

    public void deleteProductForm() {
        Utils.clearConsole(20);
        consoleHeader();
        scanner.nextLine();

        List<Product> productList = showAllProductsTable();

        System.out.println(">> Please enter the 'ID' of the product you want to delete.");
        System.out.println(">> Press 'ENTER ' if you want to go back to the main menu.");

        String productIdInput = scanner.nextLine();

        long productId;
        try {
            productId = Long.parseLong(productIdInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid product ID.");
            return;
        }

        if (productId != 0) {
            ProductServiceImpl productService = new ProductServiceImpl(productRepository);

            if (productList.stream().anyMatch(product -> product.getProductId().equals(productId))) {
                productService.deleteProduct(productId);
                System.out.println("Successfully deleted product with ID: " + productId);
            } else {
                System.out.println("Product with that ID does not exist.");
            }
        }

        Utils.returnBackToTheMainMenu(scanner);
    }

}
