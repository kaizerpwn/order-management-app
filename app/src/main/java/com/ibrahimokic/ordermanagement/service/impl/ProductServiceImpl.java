package com.ibrahimokic.ordermanagement.service.impl;

import com.ibrahimokic.ordermanagement.domain.dto.ProductDto;
import com.ibrahimokic.ordermanagement.domain.entity.Product;
import com.ibrahimokic.ordermanagement.repository.ProductRepository;
import com.ibrahimokic.ordermanagement.service.ProductService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllAvailableProducts() {
        return productRepository.findAvailableProducts(LocalDate.now());
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> updateProduct(Long productId, ProductDto updatedProductDto) {
        Optional<Product> optionalExistingProduct = productRepository.findById(productId);

        if (optionalExistingProduct.isPresent()) {
            Product existingProduct = optionalExistingProduct.get();
            BeanUtils.copyProperties(updatedProductDto, existingProduct);
            try {
                productRepository.save(existingProduct);
                return Optional.of(existingProduct);
            } catch (Exception e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteProduct(Long productId) {
        try {
            productRepository.deleteById(productId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
