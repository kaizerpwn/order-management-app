package com.ibrahimokic.ordermanagement.service.impl;

import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.domain.dto.api.LoginRequest;
import com.ibrahimokic.ordermanagement.repository.UserRepository;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.service.UserService;

import jakarta.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public ResponseEntity<?> getUserById(Long userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("User not found.");
        }
    }

    @Override
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public ResponseEntity<?> updateUser(Long userId, UserDto updatedUserDto) {
        Optional<User> optionalExistingUser = userRepository.findById(userId);

        if (optionalExistingUser.isPresent()) {
            User existingUser = optionalExistingUser.get();
            BeanUtils.copyProperties(updatedUserDto, existingUser);

            try {
                userRepository.save(existingUser);
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(existingUser);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("Internal server error.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("User not found.");
        }
    }

    @Override
    public ResponseEntity<?> deleteUser(Long userId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                userRepository.deleteById(userId);
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("User successfully deleted.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("User not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Internal server error.");
        }
    }

    @Override
    public User loginUser(LoginRequest request) {
        Optional<User> retrievedUser = Optional.ofNullable(userRepository.findByUsername(request.getUsername()));

        if(retrievedUser.isPresent()) {
            if(request.getPassword().equals(retrievedUser.get().getPassword()))
                return retrievedUser.get();

            else return null;
        }
        else {
            return null;
        }
    }
}
