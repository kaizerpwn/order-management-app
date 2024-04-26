package com.ibrahimokic.ordermanagement.mapper;

import com.ibrahimokic.ordermanagement.domain.dto.AddressDto;
import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.mapper.impl.AddressMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AddressMapperImplTest {

    private Mapper<Address, AddressDto> addressMapper;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        addressMapper = new AddressMapperImpl(modelMapper);
    }

    @Test
    void testMapTo() {
        Address address = Address.builder()
                .addressId(1L)
                .street("123 Main St")
                .city("Springfield")
                .zip("12345")
                .build();

        AddressDto addressDto = addressMapper.mapTo(address);

        assertNotNull(addressDto);
        assertEquals(address.getStreet(), addressDto.getStreet());
        assertEquals(address.getCity(), addressDto.getCity());
        assertEquals(address.getZip(), addressDto.getZip());
    }

    @Test
    void testMapFrom() {
        AddressDto addressDto = AddressDto.builder()
                .street("123 Main St")
                .city("Springfield")
                .zip("12345")
                .build();

        Address address = addressMapper.mapFrom(addressDto);

        assertNotNull(address);
        assertEquals(addressDto.getStreet(), address.getStreet());
        assertEquals(addressDto.getCity(), address.getCity());
        assertEquals(addressDto.getZip(), address.getZip());
    }
}