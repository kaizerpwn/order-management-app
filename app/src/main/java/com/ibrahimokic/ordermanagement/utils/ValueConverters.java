package com.ibrahimokic.ordermanagement.utils;

import com.ibrahimokic.ordermanagement.repository.AddressRepository;
import com.ibrahimokic.ordermanagement.service.impl.ProductServiceImpl;

public class ValueConverters {

    private final AddressRepository addressRepository;
    private final ProductServiceImpl productServiceImpl;

    public ValueConverters(AddressRepository addressRepository, ProductServiceImpl productServiceImpl) {
        this.addressRepository = addressRepository;
        this.productServiceImpl = productServiceImpl;
    }
}
