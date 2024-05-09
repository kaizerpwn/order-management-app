package com.ibrahimokic.ordermanagement.service.impl;

import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.repository.AddressRepository;
import com.ibrahimokic.ordermanagement.service.AddressService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    @Override
    public List<Address> getAllAddresses() {
        try{
            return addressRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all addresses: " + e.getMessage());
        }
    }

    @Override
    public Optional<Address> getAddressById(Long addressId) {
        try {
            return addressRepository.findById(addressId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find address with that ID: " + e.getMessage());
        }
    }

    @Override
    public Address createAddress(Address address) {
        try {
            return addressRepository.save(address);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create address: " + e.getMessage());
        }
    }


    @Override
    public boolean deleteAddress(Long addressId) {
        try {
            addressRepository.deleteById(addressId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete address: " +e.getMessage());
        }
    }
}
