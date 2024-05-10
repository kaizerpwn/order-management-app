package com.ibrahimokic.ordermanagement.mapper.impl;

import com.ibrahimokic.ordermanagement.domain.dto.ProductDto;
import com.ibrahimokic.ordermanagement.domain.entity.Product;
import com.ibrahimokic.ordermanagement.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapperImpl implements Mapper<Product, ProductDto> {
    private ModelMapper modelMapper;
    public ProductMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public ProductDto mapTo(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
    @Override
    public Product mapFrom(ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }
    public List<ProductDto> mapListToDtoList(List<Product> products) {
        return products.stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }
}
