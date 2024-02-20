package com.ibrahimokic.ordermanagement.utils;

import com.ibrahimokic.ordermanagement.domain.Address;
import com.ibrahimokic.ordermanagement.domain.User;
import com.ibrahimokic.ordermanagement.domain.dto.UserDto;

public class Utils {
    public static User convertDtoToUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setBirthDate(userDto.getBirthDate());

        Address address = new Address();
        address.setStreet(userDto.getAddressStreet());
        address.setZip(userDto.getAddressZip());
        address.setCity(userDto.getAddressCity());
        address.setCountry(userDto.getAddressCountry());
        user.setAddress(address);

        return user;
    }
}
