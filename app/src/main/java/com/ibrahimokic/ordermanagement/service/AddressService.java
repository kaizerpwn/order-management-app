package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.entity.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    List<Address> getAllAddresses();

    Optional<Address> getAddressById(Long addressId);

    Address createAddress(Address address);

    Address updateAddress(Long addressId, Address newAddress);

    void deleteAddress(Long addressId);
}
