package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.Address;
import com.ibrahimokic.ordermanagement.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository){
        this.addressRepository = addressRepository;
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Optional<Address> getAddressById(Long addressId){
        return addressRepository.findById(addressId);
    }

    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    public Address updateAddress(Long addressId, Address newAddress) {
        if(addressRepository.existsById(addressId)){
            newAddress.setAddressId(addressId);
            return addressRepository.save(newAddress);
        }
        else {
            return null;
        }
    }

    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }
}
