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
    void testGetAddressById()
    {
        Address newAddress = new Address();
        newAddress.setCity("Sarajevo");
        newAddress.setZip("71000");
        newAddress.setCountry("Bosnia and Herzegovina");
        newAddress.setStreet("Dzemala Bijedica");

        addressRepository.save(newAddress);

        Optional<Address> retrievedAddressFromDatabase = addressRepository.findById(newAddress.getAddressId());

        assertTrue(retrievedAddressFromDatabase.isPresent());
        assertEquals(newAddress.getAddressId(), retrievedAddressFromDatabase.get().getAddressId());
    }

    @Test
    void testGetAllAddresses(){
        Address addressOne = new Address();
        addressOne.setCity("Sarajevo");
        addressOne.setZip("71000");
        addressOne.setCountry("Bosnia and Herzegovina");
        addressOne.setStreet("Dzemala Bijedica");

        addressRepository.save(addressOne);

        Address addressTwo = new Address();
        addressTwo.setCity("Tuzla");
        addressTwo.setZip("75000");
        addressTwo.setCountry("Bosnia and Herzegovina");
        addressTwo.setStreet("Skojevska");

        addressRepository.save(addressTwo);

        List<Address> allRetrievedAddresses = addressRepository.findAll();

        assertEquals(2, allRetrievedAddresses.size());
        assertTrue(allRetrievedAddresses.stream().anyMatch(address -> address.getCity().equals("Tuzla")));
        assertTrue(allRetrievedAddresses.stream().anyMatch(address -> address.getCity().equals("Sarajevo")));
    }

    @Test
    void testDeleteAddress() {
        Address newAddress = new Address();
        newAddress.setCity("Tuzla");
        newAddress.setZip("75000");
        newAddress.setCountry("Bosnia and Herzegovina");
        newAddress.setStreet("Skojevska");

        addressRepository.save(newAddress);

        Optional<Address> retrievedAddress = addressRepository.findById(newAddress.getAddressId());

        assertTrue(retrievedAddress.isPresent());

        addressRepository.deleteById(newAddress.getAddressId());

        List<Address> allRetrievedAddresses = addressRepository.findAll();
        assertEquals(0, allRetrievedAddresses.size());
    }
}
