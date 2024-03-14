package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.repository.UserRepository;
import com.ibrahimokic.ordermanagement.service.impl.UserServiceImpl;
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
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetAllUsers() {
        User user = User.builder()
                .email("ibrahim@gmail.com")
                .role("admin")
                .password("123123")
                .username("ibrahim")
                .build();

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> retrievedUsers = userService.getAllUsers();

        verify(userRepository, times(1)).findAll();
        assertEquals(1, retrievedUsers.size());
        assertEquals(user.getEmail(), retrievedUsers.get(0).getEmail());
    }

    @Test
    void testGetUserById() {
        User mockUser = User.builder().email("ibrahim@gmail.com").build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        Optional<User> retrievedUser = userService.getUserById(1L);

        verify(userRepository, times(1)).findById(1L);
        assertTrue(retrievedUser.isPresent());
        assertEquals(mockUser.getEmail(), retrievedUser.get().getEmail());
    }

    @Test
    void testCreateUser() {
        User mockUser = User.builder().build();
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User createdUser = userService.createUser(mockUser);

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(mockUser, createdUser);
    }
}
