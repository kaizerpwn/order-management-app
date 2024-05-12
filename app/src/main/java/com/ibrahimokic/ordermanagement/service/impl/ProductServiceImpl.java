package com.ibrahimokic.ordermanagement.service.impl;

import com.ibrahimokic.ordermanagement.domain.dto.ProductDto;
import com.ibrahimokic.ordermanagement.domain.entity.Product;
import com.ibrahimokic.ordermanagement.mapper.impl.ProductMapperImpl;
import com.ibrahimokic.ordermanagement.repository.OrderItemRepository;
import com.ibrahimokic.ordermanagement.repository.ProductRepository;
import com.ibrahimokic.ordermanagement.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductMapperImpl productMapper;

    @Override
    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all products: " + e.getMessage());
        }
    }

    @Override
    public List<Product> getAllAvailableProducts() {
        try {
            return productRepository.findAvailableProducts(LocalDate.now());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all available products: " + e.getMessage());
        }
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        try {
            return productRepository.findById(productId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product by ID: " + e.getMessage());
        }
    }

    @Override
    public Product createProduct(Product product) {
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new product: " + e.getMessage());
        }
    }

    @Override
    public Optional<Product> updateProduct(Long productId, @Valid ProductDto updatedProductDto) {
        try {
            Optional<Product> optionalExistingProduct = productRepository.findById(productId);

            if (optionalExistingProduct.isPresent()) {
                Product existingProduct = productMapper.mapFrom(updatedProductDto);
                existingProduct.setProductId(productId);
                try {
                    productRepository.save(existingProduct);
                    return Optional.of(existingProduct);
                } catch (Exception e) {
                    throw new RuntimeException("Product not found");
                }
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update product: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteProduct(Long productId) {
        try {
            if(productRepository.existsById(productId)) {
                orderItemRepository.deleteByProductId(productId);
                productRepository.deleteById(productId);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete product by ID: " + e.getMessage());
        }
    }
}
