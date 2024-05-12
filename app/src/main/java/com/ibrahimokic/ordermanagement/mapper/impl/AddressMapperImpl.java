package com.ibrahimokic.ordermanagement.mapper.impl;

import com.ibrahimokic.ordermanagement.domain.dto.AddressDto;
import com.ibrahimokic.ordermanagement.domain.entity.Address;
import com.ibrahimokic.ordermanagement.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AddressMapperImpl implements Mapper<Address, AddressDto> {
    private final ModelMapper modelMapper;
    public AddressMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public AddressDto mapTo(Address address) {
        return modelMapper.map(address, AddressDto.class);
    }
    @Override
    public Address mapFrom(AddressDto addressDto) {
        return modelMapper.map(addressDto, Address.class);
    }
    public List<AddressDto> mapListToDtoList(List<Address> addresses) {
        return addresses.stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }
}
