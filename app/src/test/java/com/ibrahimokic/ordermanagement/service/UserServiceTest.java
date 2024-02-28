package com.ibrahimokic.ordermanagement.service;
import com.ibrahimokic.ordermanagement.domain.User;
import com.ibrahimokic.ordermanagement.repository.UserRepository;
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
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetAllUsers() {
        User newUser = new User();
        newUser.setEmail("ibrahim@gmail.com");
        newUser.setRole("admin");
        newUser.setPassword("123123");
        newUser.setUsername("ibrahim");

        when(userRepository.findAll()).thenReturn(Collections.singletonList(newUser));

        List<User> retrievedUsers = userService.getAllUsers();

        verify(userRepository, times(1)).findAll();

        assertEquals(1, retrievedUsers.size());
        assertEquals(newUser.getEmail(), retrievedUsers.get(0).getEmail());
    }
    @Test
    void testGetUserById() {
        User mockUser = mock(User.class);
        when(mockUser.getEmail()).thenReturn("ibrahim@gmail.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        Optional<User> retrievedUser = userService.getUserById(1L);

        verify(userRepository, times(1)).findById(1L);

        assertTrue(retrievedUser.isPresent());
        assertEquals(mockUser.getEmail(), retrievedUser.get().getEmail());
    }
    @Test
    void testCreateUser() {
        User mockUser = mock(User.class);

        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User createdUser = userService.createUser(mockUser);

        verify(userRepository, times(1)).save(any(User.class));

        assertEquals(mockUser, createdUser);
    }
}
