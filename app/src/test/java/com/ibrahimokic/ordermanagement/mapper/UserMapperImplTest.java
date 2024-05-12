package com.ibrahimokic.ordermanagement.mapper;

import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.mapper.impl.UserMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

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

    @Test
    void testMapListToEntityList() {
        List<UserDto> userDtos = Arrays.asList(
                UserDto.builder().userId(1L).username("user1").email("user1@example.com").build(),
                UserDto.builder().userId(2L).username("user2").email("user2@example.com").build(),
                UserDto.builder().userId(3L).username("user3").email("user3@example.com").build()
        );

        List<User> users = userMapper.mapListToEntityList(userDtos);

        assertNotNull(users);
        assertEquals(3, users.size());

        for (int i = 0; i < userDtos.size(); i++) {
            UserDto userDto = userDtos.get(i);
            User user = users.get(i);
            assertEquals(userDto.getUserId(), user.getUserId());
            assertEquals(userDto.getUsername(), user.getUsername());
            assertEquals(userDto.getEmail(), user.getEmail());
        }
    }

    @Test
    void testMapUserListToDtoList() {
        List<User> users = Arrays.asList(
                User.builder().userId(1L).username("user1").email("user1@example.com").build(),
                User.builder().userId(2L).username("user2").email("user2@example.com").build(),
                User.builder().userId(3L).username("user3").email("user3@example.com").build()
        );

        List<UserDto> userDtos = userMapper.mapUserListToDtoList(users);

        assertNotNull(userDtos);
        assertEquals(3, userDtos.size());
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            UserDto userDto = userDtos.get(i);
            assertEquals(user.getUserId(), userDto.getUserId());
            assertEquals(user.getUsername(), userDto.getUsername());
            assertEquals(user.getEmail(), userDto.getEmail());
        }
    }
}
