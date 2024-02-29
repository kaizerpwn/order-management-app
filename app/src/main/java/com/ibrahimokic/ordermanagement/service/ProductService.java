package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.entity.Product;
import com.ibrahimokic.ordermanagement.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    public Product createProduct(Product product){
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Product newProduct) {
        if(productRepository.existsById(productId)) {
            newProduct.setProductId(productId);
            return productRepository.save(newProduct);
        }
        else {
            return null;
        }
    }

    public void deleteProduct(Long productId){
        productRepository.deleteById(productId);
    }
}
