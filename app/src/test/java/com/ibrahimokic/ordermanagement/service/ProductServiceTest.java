package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.Product;
import com.ibrahimokic.ordermanagement.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testGetAllProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1L, "Table", new BigDecimal(100.0), LocalDate.now(), LocalDate.now().plusDays(30), 100));
        productList.add(new Product(2L, "Chair", new BigDecimal(150.0), LocalDate.now(), LocalDate.now().plusDays(30), 150));

        when(productRepository.findAll()).thenReturn(productList);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
    }

    @Test
    void testGetProductById() {
        Long productId = 1L;
        Product product = new Product(productId, "Chair", new BigDecimal(150.0), LocalDate.now(), LocalDate.now().plusDays(30), 100);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(productId);

        assertEquals(product, result.orElse(null));
    }

    @Test
    void testCreateProduct() {
        Product product = new Product(1L, "Chair", new BigDecimal(150.0), LocalDate.now(), LocalDate.now().plusDays(30), 100);

        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.createProduct(product);

        assertEquals(product, result);
    }

    @Test
    void testUpdateProduct() {
        Long productId = 1L;
        Product updatedProduct = new Product(productId, "Table", BigDecimal.valueOf(150.0), LocalDate.now(), LocalDate.now().plusDays(30), 100);

        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);

        Product result = productService.updateProduct(productId, updatedProduct);

        assertEquals(updatedProduct, result);
    }

    @Test
    void testUpdateProductWhenProductNotExists() {
        Long productId = 1L;
        Product updatedProduct = new Product(productId, "Chair", BigDecimal.valueOf(150.0),
                LocalDate.now(), LocalDate.now().plusDays(30), 100);
        when(productRepository.existsById(productId)).thenReturn(false);

        Product result = productService.updateProduct(productId, updatedProduct);

        assertFalse(productRepository.existsById(productId));
        assertNull(result);
    }

    @Test
    void testDeleteProduct() {
        Long productId = 1L;

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }
}
