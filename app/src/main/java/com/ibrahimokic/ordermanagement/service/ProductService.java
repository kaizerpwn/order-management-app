package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.dto.ProductDto;
import com.ibrahimokic.ordermanagement.domain.entity.Product;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<?> getAllProducts();

    Optional<Product> getProductById(Long productId);

    Product createProduct(Product product);

    ResponseEntity<?> updateProduct(Long productId, ProductDto updateProductDto);

    void deleteProduct(Long productId);
}
