package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.Address;
import com.ibrahimokic.ordermanagement.repositories.AddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    @Test
    void testGetAllAddresses(){
        Address newAddress = new Address();
        newAddress.setStreet("Dzemala Bijedica bb");
        newAddress.setCountry("Bosnia and Herzegovina");
        newAddress.setZip("75000");
        newAddress.setCity("Sarajevo");

        when(addressRepository.findAll()).thenReturn(Collections.singletonList(newAddress));

        List<Address> retrievedAddresses = addressService.getAllAddresses();

        verify(addressRepository, times(1)).findAll();

        assertEquals(1, retrievedAddresses.size());
        assertEquals(newAddress.getStreet(), retrievedAddresses.get(0).getStreet());
    }

    @Test
    void testGetUserById(){
        Address mockAddress = mock(Address.class);
        when(mockAddress.getStreet()).thenReturn("Sarajevo");

        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(mockAddress));

        Optional<Address> retrievedAddress = addressService.getAddressById(1L);

        verify(addressRepository, times(1)).findById(1L);

        assertTrue(retrievedAddress.isPresent());
        assertEquals(mockAddress.getStreet(), retrievedAddress.get().getStreet());
    }

    @Test
    void testCreateAddress(){
        Address mockAddress = mock(Address.class);
        when(addressRepository.save(any(Address.class))).thenReturn(mockAddress);

        Address createdAddress = addressService.createAddress(mockAddress);
        verify(addressRepository, times(1)).save(any(Address.class));
        assertEquals(mockAddress, createdAddress);
    }
}
