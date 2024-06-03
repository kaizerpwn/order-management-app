package com.ibrahimokic.ordermanagement.mapper;

import com.ibrahimokic.ordermanagement.domain.dto.ProductDto;
import com.ibrahimokic.ordermanagement.domain.entity.Product;
import com.ibrahimokic.ordermanagement.mapper.impl.ProductMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductMapperImplTest {

    private ProductMapperImpl productMapper;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        productMapper = new ProductMapperImpl(modelMapper);
    }

    @Test
    void testMapTo() {
        Product product = Product.builder()
                .productId(1L)
                .productName("Product 1")
                .price(new BigDecimal("20.00"))
                .build();

        ProductDto productDto = productMapper.mapTo(product);

        assertNotNull(productDto);
        assertEquals(product.getProductId(), productDto.getProductId());
        assertEquals(product.getProductName(), productDto.getProductName());
        assertEquals(product.getPrice(), productDto.getPrice());
    }

    @Test
    void testMapFrom() {
        ProductDto productDto = ProductDto.builder()
                .productName("Product 2")
                .price(new BigDecimal("30.00"))
                .build();

        Product product = productMapper.mapFrom(productDto);

        assertNotNull(product);
        assertEquals(productDto.getProductName(), product.getProductName());
        assertEquals(productDto.getPrice(), product.getPrice());
    }
}
