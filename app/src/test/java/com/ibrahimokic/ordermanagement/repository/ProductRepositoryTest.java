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
    void testGetProductById(){
        Product newProduct = new Product();
        newProduct.setProductName("Chair");
        newProduct.setPrice(new BigDecimal(120));
        newProduct.setAvailableQuantity(10);

        newProduct.setAvailableFrom(LocalDate.now());
        newProduct.setAvailableUntil(LocalDate.now().plusDays(1));

        productRepository.save(newProduct);

        Optional<Product> retrievedProduct = productRepository.findById(newProduct.getProductId());

        assertTrue(retrievedProduct.isPresent());
        assertEquals(retrievedProduct.get().getProductName(), newProduct.getProductName());
    }

    @Test
    void testGetAllProducts() {
        Product productOne = new Product();
        productOne.setProductName("Chair");
        productOne.setPrice(new BigDecimal(120));
        productOne.setAvailableQuantity(10);
        productOne.setAvailableFrom(LocalDate.now());
        productOne.setAvailableUntil(LocalDate.now().plusDays(1));

        productRepository.save(productOne);

        Product productTwo = new Product();
        productTwo.setProductName("Table");
        productTwo.setPrice(new BigDecimal(160));
        productTwo.setAvailableQuantity(5);
        productTwo.setAvailableFrom(LocalDate.now());
        productTwo.setAvailableUntil(LocalDate.now().plusDays(1));

        productRepository.save(productTwo);

        List<Product> retrievedProductsList = productRepository.findAll();
        assertEquals(2, retrievedProductsList.size());
        assertTrue(retrievedProductsList.stream().anyMatch(product -> product.getProductName().equals("Chair")));
        assertTrue(retrievedProductsList.stream().anyMatch(product -> product.getProductName().equals("Table")));
    }

    @Test
    void testDeleteProduct()
    {
        Product testProduct = new Product();
        testProduct.setProductName("Table");
        testProduct.setPrice(new BigDecimal(160));
        testProduct.setAvailableQuantity(5);
        testProduct.setAvailableFrom(LocalDate.now());
        testProduct.setAvailableUntil(LocalDate.now().plusDays(1));

        productRepository.save(testProduct);

        Optional<Product> retrievedProduct = productRepository.findById(testProduct.getProductId());

        assertTrue(retrievedProduct.isPresent());

        productRepository.deleteById(testProduct.getProductId());

        List<Product> retrievedProductList = productRepository.findAll();

        assertEquals(0, retrievedProductList.size());
    }
}
