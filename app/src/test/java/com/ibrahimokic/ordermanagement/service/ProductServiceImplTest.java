package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.dto.ProductDto;
import com.ibrahimokic.ordermanagement.domain.entity.Product;
import com.ibrahimokic.ordermanagement.mapper.impl.ProductMapperImpl;
import com.ibrahimokic.ordermanagement.repository.OrderItemRepository;
import com.ibrahimokic.ordermanagement.repository.ProductRepository;
import com.ibrahimokic.ordermanagement.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductMapperImpl productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

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

    @Test
    void testDeleteProduct() {
        Long productId = 1L;
        when(productRepository.existsById(1L)).thenReturn(true);
        productService.deleteProduct(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Collections.singletonList(
                Product.builder().productId(1L).productName("Chair").price(new BigDecimal("150.0")).build());
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();
        assertEquals(products, result);
    }

    @Test
    void testGetAllAvailableProducts() {
        List<Product> products = Collections.singletonList(
                Product.builder().productId(1L).productName("Chair").price(new BigDecimal("150.0")).build());
        when(productRepository.findAvailableProducts(LocalDate.now())).thenReturn(products);

        List<Product> result = productService.getAllAvailableProducts();
        assertEquals(products, result);
    }

    @Test
    void testUpdateProduct() {
        Long productId = 1L;
        Product existingProduct = Product.builder()
                .productId(productId)
                .productName("Chair")
                .price(new BigDecimal("150.0"))
                .build();
        ProductDto updatedProductDto = new ProductDto();
        updatedProductDto.setProductName("Table");
        updatedProductDto.setPrice(new BigDecimal("200.0"));

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        lenient().when(productMapper.mapFrom(updatedProductDto)).thenReturn(existingProduct);

        Optional<Product> result = productService.updateProduct(productId, updatedProductDto);

        assertTrue(result.isPresent());
        Product updatedProduct = result.get();
        assertEquals(existingProduct.getProductName(), updatedProduct.getProductName());
        assertEquals(existingProduct.getPrice(), updatedProduct.getPrice());

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(existingProduct);
    }

}

