package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.repository.AddressRepository;
import com.ibrahimokic.ordermanagement.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Test
    void testGetAllAddresses() {
        Address address = Address.builder()
                .street("Dzemala Bijedica bb")
                .country("Bosnia and Herzegovina")
                .zip("75000")
                .city("Sarajevo")
                .build();

        when(addressRepository.findAll()).thenReturn(Collections.singletonList(address));

        List<Address> retrievedAddresses = addressService.getAllAddresses();

        verify(addressRepository, times(1)).findAll();
        assertEquals(1, retrievedAddresses.size());
        assertEquals(address.getStreet(), retrievedAddresses.get(0).getStreet());
    }

    @Test
    void testGetUserById() {
        Address mockAddress = Address.builder().street("Dzemala Bijedica").build();
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(mockAddress));

        Optional<Address> retrievedAddress = addressService.getAddressById(1L);

        verify(addressRepository, times(1)).findById(1L);
        assertTrue(retrievedAddress.isPresent());
        assertEquals(mockAddress.getStreet(), retrievedAddress.get().getStreet());
    }

    @Test
    void testCreateAddress() {
        Address mockAddress = Address.builder().build();
        when(addressRepository.save(any(Address.class))).thenReturn(mockAddress);

        Address createdAddress = addressService.createAddress(mockAddress);

        verify(addressRepository, times(1)).save(any(Address.class));
        assertEquals(mockAddress, createdAddress);
    }

    @Test
    void testDeleteAddressShouldSuccess() {
        when(addressRepository.existsById(1L)).thenReturn(true);

        boolean result = addressService.deleteAddress(1L);
        assertTrue(result);
    }

    @Test
    void testDeleteAddressShouldFail() {
        boolean result = addressService.deleteAddress(500L);
        assertFalse(result);
    }
}
