package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.domain.dto.api.LoginRequest;
import com.ibrahimokic.ordermanagement.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long userId);
    User createUser(User user);
    User loginUser(LoginRequest request);
    Optional<User> updateUser(Long userId, UserDto updatedUserDto);
    boolean deleteUser(Long userId);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long userId);
    Optional<User> findByUsername(String username);
}
