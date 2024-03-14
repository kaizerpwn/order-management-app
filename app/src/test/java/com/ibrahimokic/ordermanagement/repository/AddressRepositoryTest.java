package com.ibrahimokic.ordermanagement.repository;

import com.ibrahimokic.ordermanagement.domain.entity.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AddressRepositoryTest {
    @Autowired
    private AddressRepository addressRepository;

    @Test
    void testGetAddressById() {
        Address newAddress = Address.builder()
                .city("Sarajevo")
                .zip("71000")
                .country("Bosnia and Herzegovina")
                .street("Dzemala Bijedica")
                .build();

        addressRepository.save(newAddress);

        Optional<Address> retrievedAddressFromDatabase = addressRepository.findById(newAddress.getAddressId());

        assertTrue(retrievedAddressFromDatabase.isPresent());
        assertEquals(newAddress.getAddressId(), retrievedAddressFromDatabase.get().getAddressId());
    }

    @Test
    void testGetAllAddresses() {
        Address addressOne = Address.builder()
                .city("Sarajevo")
                .zip("71000")
                .country("Bosnia and Herzegovina")
                .street("Dzemala Bijedica")
                .build();

        addressRepository.save(addressOne);

        Address addressTwo = Address.builder()
                .city("Tuzla")
                .zip("75000")
                .country("Bosnia and Herzegovina")
                .street("Skojevska")
                .build();

        addressRepository.save(addressTwo);

        List<Address> allRetrievedAddresses = addressRepository.findAll();

        assertEquals(2, allRetrievedAddresses.size());
        assertTrue(allRetrievedAddresses.stream().anyMatch(address -> address.getCity().equals("Tuzla")));
        assertTrue(allRetrievedAddresses.stream().anyMatch(address -> address.getCity().equals("Sarajevo")));
    }

    @Test
    void testDeleteAddress() {
        Address newAddress = Address.builder()
                .city("Tuzla")
                .zip("75000")
                .country("Bosnia and Herzegovina")
                .street("Skojevska")
                .build();

        addressRepository.save(newAddress);

        Optional<Address> retrievedAddress = addressRepository.findById(newAddress.getAddressId());

        assertTrue(retrievedAddress.isPresent());

        addressRepository.deleteById(newAddress.getAddressId());

        List<Address> allRetrievedAddresses = addressRepository.findAll();
        assertEquals(0, allRetrievedAddresses.size());
    }
}
