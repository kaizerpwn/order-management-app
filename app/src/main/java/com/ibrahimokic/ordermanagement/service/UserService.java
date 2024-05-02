package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.domain.dto.api.LoginRequest;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    ResponseEntity<?> getUserById(Long userId);
    User createUser(User user);
    User loginUser(LoginRequest request);
    ResponseEntity<?> updateUser(Long userId, UserDto updatedUserDto);
    ResponseEntity<?> deleteUser(Long userId);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long userId);
    Optional<User> findByUsername(String username);
}
