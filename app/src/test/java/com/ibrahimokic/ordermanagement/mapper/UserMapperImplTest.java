package com.ibrahimokic.ordermanagement.mapper;

import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.mapper.impl.UserMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMapperImplTest {

    private UserMapperImpl userMapper;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        userMapper = new UserMapperImpl(modelMapper);
    }

    @Test
    void testMapTo() {
        User user = User.builder()
                .userId(1L)
                .username("user1")
                .email("user1@example.com")
                .build();

        UserDto userDto = userMapper.mapTo(user);

        assertNotNull(userDto);
        assertEquals(user.getUserId(), userDto.getUserId());
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    void testMapFrom() {
        UserDto userDto = UserDto.builder()
                .username("user2")
                .email("user2@example.com")
                .build();

        User user = userMapper.mapFrom(userDto);

        assertNotNull(user);
        assertEquals(userDto.getUsername(), user.getUsername());
        assertEquals(userDto.getEmail(), user.getEmail());
    }
}
