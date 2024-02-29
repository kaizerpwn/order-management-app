package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();

    Optional<Product> getProductById(Long productId);

    Product createProduct(Product product);

    Product updateProduct(Long productId, Product newProduct);

    void deleteProduct(Long productId);
}
