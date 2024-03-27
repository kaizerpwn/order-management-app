package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.domain.dto.api.LoginRequest;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    ResponseEntity<?> getUserById(Long userId);

    User createUser(User user);

    User loginUser(LoginRequest request);

    ResponseEntity<?> updateUser(Long userId, UserDto updatedUserDto);

    ResponseEntity<?> deleteUser(Long userId);
}
