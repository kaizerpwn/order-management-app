package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.dto.ProductDto;
import com.ibrahimokic.ordermanagement.domain.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> getAllAvailableProducts();
    Optional<Product> getProductById(Long productId);
    Product createProduct(Product product);
    Optional<Product> updateProduct(Long productId, ProductDto updateProductDto);
    boolean deleteProduct(Long productId);
}
