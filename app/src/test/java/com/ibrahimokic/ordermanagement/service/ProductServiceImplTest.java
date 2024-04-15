package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.entity.Product;
import com.ibrahimokic.ordermanagement.repository.ProductRepository;
import com.ibrahimokic.ordermanagement.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    // @Test
    // void testGetAllProducts() {
    //     List<Product> productList = List.of(
    //             Product.builder().productId(1L).productName("Table").price(new BigDecimal("100.0")).build(),
    //             Product.builder().productId(2L).productName("Chair").price(new BigDecimal("150.0")).build()
    //     );
    //     when(productRepository.findAll()).thenReturn(productList);

    //     List<Product> result = productService.getAllProducts();

    //     assertEquals(2, result.size());
    // }

    @Test
    void testGetProductById() {
        Long productId = 1L;
        Product product = Product.builder().productId(productId).productName("Chair").price(new BigDecimal("150.0")).build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(productId);

        assertEquals(product, result.orElse(null));
    }

    @Test
    void testCreateProduct() {
        Product product = Product.builder().productId(1L).productName("Chair").price(new BigDecimal("150.0")).build();
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.createProduct(product);

        assertEquals(product, result);
    }

    // @Test
    // void testUpdateProduct() {
    //     Long productId = 1L;
    //     Product updatedProduct = Product.builder().productId(productId).productName("Table").price(new BigDecimal("150.0")).build();
    //     when(productRepository.existsById(productId)).thenReturn(true);
    //     when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);

    //     Product result = productService.updateProduct(productId, updatedProduct);

    //     assertEquals(updatedProduct, result);
    // }

    // @Test
    // void testUpdateProductWhenProductNotExists() {
    //     Long productId = 1L;
    //     Product updatedProduct = Product.builder().productId(productId).productName("Chair").price(new BigDecimal("150.0")).build();
    //     when(productRepository.existsById(productId)).thenReturn(false);

    //     Product result = productService.updateProduct(productId, updatedProduct);

    //     assertFalse(productRepository.existsById(productId));
    //     assertNull(result);
    // }

    @Test
    void testDeleteProduct() {
        Long productId = 1L;

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }
}

