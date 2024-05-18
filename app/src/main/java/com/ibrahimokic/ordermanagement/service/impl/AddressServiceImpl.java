package com.ibrahimokic.ordermanagement.service.impl;

import com.ibrahimokic.ordermanagement.domain.dto.AddressDto;
import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.mapper.impl.AddressMapperImpl;
import com.ibrahimokic.ordermanagement.repository.AddressRepository;
import com.ibrahimokic.ordermanagement.service.AddressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapperImpl addressMapper;

    @Override
    public List<Address> getAllAddresses() {
        try {
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
            if (addressRepository.existsById(addressId)) {
                addressRepository.deleteById(addressId);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete address: " + e.getMessage());
        }
    }

    @Override
    public AddressDto updateAddress(Long addressId, @Valid AddressDto addressDto) {
        try {
            Optional<Address> optionalExistingAddress = addressRepository.findById(addressId);

            if (optionalExistingAddress.isPresent()) {
                Address existingAddress = addressMapper.mapFrom(addressDto);
                existingAddress.setAddressId(addressId);
                try {
                    addressRepository.save(existingAddress);
                    return addressMapper.mapTo(existingAddress);
                } catch (Exception e) {
                    throw new RuntimeException("An error occurred while updating an address");
                }
            } else {
                throw new RuntimeException("Address with that ID does not exist in database");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
