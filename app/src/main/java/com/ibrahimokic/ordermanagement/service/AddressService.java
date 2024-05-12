package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.dto.AddressDto;
import com.ibrahimokic.ordermanagement.domain.entity.Address;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    List<Address> getAllAddresses();
    Optional<Address> getAddressById(Long addressId);
    Address createAddress(Address address);
    boolean deleteAddress(Long addressId);
    AddressDto updateAddress(Long addressId, @Valid AddressDto addressDto);
}
