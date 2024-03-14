package com.ibrahimokic.ordermanagement.repository;

import com.ibrahimokic.ordermanagement.domain.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void testGetProductById() {
        Product newProduct = Product.builder()
                .productName("Chair")
                .price(new BigDecimal(120))
                .availableQuantity(10)
                .availableFrom(LocalDate.now())
                .availableUntil(LocalDate.now().plusDays(1))
                .build();

        productRepository.save(newProduct);

        Optional<Product> retrievedProduct = productRepository.findById(newProduct.getProductId());

        assertTrue(retrievedProduct.isPresent());
        assertEquals(retrievedProduct.get().getProductName(), newProduct.getProductName());
    }

    @Test
    void testGetAllProducts() {
        Product productOne = Product.builder()
                .productName("Chair")
                .price(new BigDecimal(120))
                .availableQuantity(10)
                .availableFrom(LocalDate.now())
                .availableUntil(LocalDate.now().plusDays(1))
                .build();

        productRepository.save(productOne);

        Product productTwo = Product.builder()
                .productName("Table")
                .price(new BigDecimal(160))
                .availableQuantity(5)
                .availableFrom(LocalDate.now())
                .availableUntil(LocalDate.now().plusDays(1))
                .build();

        productRepository.save(productTwo);

        List<Product> retrievedProductsList = productRepository.findAll();
        assertEquals(2, retrievedProductsList.size());
        assertTrue(retrievedProductsList.stream().anyMatch(product -> product.getProductName().equals("Chair")));
        assertTrue(retrievedProductsList.stream().anyMatch(product -> product.getProductName().equals("Table")));
    }

    @Test
    void testDeleteProduct() {
        Product testProduct = Product.builder()
                .productName("Table")
                .price(new BigDecimal(160))
                .availableQuantity(5)
                .availableFrom(LocalDate.now())
                .availableUntil(LocalDate.now().plusDays(1))
                .build();

        productRepository.save(testProduct);

        Optional<Product> retrievedProduct = productRepository.findById(testProduct.getProductId());

        assertTrue(retrievedProduct.isPresent());

        productRepository.deleteById(testProduct.getProductId());

        List<Product> retrievedProductList = productRepository.findAll();

        assertEquals(0, retrievedProductList.size());
    }
}
