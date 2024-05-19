package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.repository.UserRepository;
import com.ibrahimokic.ordermanagement.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

        Optional<User> responseEntity = userService.getUserById(1L);

        verify(userRepository, times(1)).findById(1L);
        assertTrue(responseEntity.isPresent());

        User retrievedUser = responseEntity.get();

        assertNotNull(retrievedUser);
        assertEquals(mockUser.getEmail(), retrievedUser.getEmail());
    }

    @Test
    void testCreateUser() {
        User mockUser = User.builder().build();
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User createdUser = userService.createUser(mockUser);

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(mockUser, createdUser);
    }

    @Test
    void testFindByUsername() {
        String username = "ibrahim";
        User mockUser = User.builder().username(username).build();

        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        Optional<User> foundUser = userService.findByUsername(username);

        verify(userRepository, times(1)).findByUsername(username);

        assertTrue(foundUser.isPresent());
        assertEquals(username, foundUser.get().getUsername());
    }

    @Test
    void testFindById() {
        Long userId = 1L;
        User mockUser = User.builder().userId(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        Optional<User> foundUser = userService.findById(userId);

        verify(userRepository, times(1)).findById(userId);

        assertTrue(foundUser.isPresent());
        assertEquals(userId, foundUser.get().getUserId());
    }

    @Test
    void testFindByEmail() {
        String email = "ibrahim@gmail.com";
        User mockUser = User.builder().email(email).build();

        when(userRepository.findByEmail(email)).thenReturn(mockUser);

        Optional<User> foundUser = userService.findByEmail(email);

        verify(userRepository, times(1)).findByEmail(email);

        assertTrue(foundUser.isPresent());
        assertEquals(email, foundUser.get().getEmail());
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        UserDto updatedUserDto = new UserDto();
        User mockUser = User.builder().build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        Optional<User> updatedUser = userService.updateUser(userId, updatedUserDto);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));

        assertTrue(updatedUser.isPresent());
        assertEquals(mockUser, updatedUser.get());
    }

    @Test
    void testCreateUser_UserWithEmailExists() {
        User existingUser = User.builder().email("existing@example.com").build();
        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);

        assertThrows(RuntimeException.class, () -> userService.createUser(existingUser));
    }

    @Test
    void testUpdateUser_UserNotExists() {
        Long userId = 1L;
        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setUsername("new_username");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(userId, updatedUserDto));
    }

    @Test
    void testDeleteUserByUsername_UserExists() {
        String username = "existingUser";
        User mockUser = User.builder().username(username).build();

        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        boolean result = userService.deleteUserByUsername(username);

        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).delete(mockUser);

        assertTrue(result);
    }

    @Test
    void testDeleteUserByUsername_UserDoesNotExist() {
        String username = "nonExistingUser";

        when(userRepository.findByUsername(username)).thenReturn(null);

        boolean result = userService.deleteUserByUsername(username);

        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(0)).delete(any(User.class));

        assertFalse(result);
    }

    @Test
    void testDeleteUserByUsername_DataIntegrityViolationException() {
        String username = "userWithReferences";
        User mockUser = User.builder().username(username).build();

        when(userRepository.findByUsername(username)).thenReturn(mockUser);
        doThrow(new DataIntegrityViolationException("User is still referenced")).when(userRepository).delete(mockUser);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.deleteUserByUsername(username));

        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).delete(mockUser);

        assertEquals("Cannot delete user because he is still referenced by some order.", exception.getMessage());
    }

    @Test
    void testDeleteUserByUsername_OtherException() {
        String username = "userWithException";
        User mockUser = User.builder().username(username).build();

        when(userRepository.findByUsername(username)).thenReturn(mockUser);
        doThrow(new RuntimeException("Unexpected error")).when(userRepository).delete(mockUser);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.deleteUserByUsername(username));

        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).delete(mockUser);

        assertEquals("Failed to delete user by username: Unexpected error", exception.getMessage());
    }
}
