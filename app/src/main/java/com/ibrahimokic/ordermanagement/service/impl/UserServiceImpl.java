package com.ibrahimokic.ordermanagement.service.impl;

import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.domain.dto.api.LoginRequest;
import com.ibrahimokic.ordermanagement.repository.UserRepository;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all users: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        try {
            return userRepository.findById(userId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user by ID: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public User createUser(User user) {
        try {
            User userExists = userRepository.findByEmail(user.getEmail());

            if (userExists != null) {
                throw new RuntimeException("User with that email already exists in the database.");
            }

            return userRepository.save(user);
        } catch (ConstraintViolationException e) {
            StringBuilder errorMessages = new StringBuilder("Validation failed with the following errors:\n");
            e.getConstraintViolations()
                    .forEach(violation -> errorMessages.append(" - ").append(violation.getMessage()).append("\n"));
            throw new RuntimeException(errorMessages.toString());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<User> updateUser(Long userId, UserDto updatedUserDto) {
        try {
            Optional<User> retrievedUser = userRepository.findById(userId);

            if (retrievedUser.isPresent()) {
                User user = retrievedUser.get();
                BeanUtils.copyProperties(updatedUserDto, user, "userId");

                userRepository.save(user);
                return Optional.of(user);
            } else {
                throw new RuntimeException("User with ID " + userId + " does not exist in the database.");
            }
        } catch (ConstraintViolationException e) {
            StringBuilder errorMessages = new StringBuilder("Validation failed with the following errors:\n");
            e.getConstraintViolations()
                    .forEach(violation -> errorMessages.append(" - ").append(violation.getMessage()).append("\n"));
            throw new RuntimeException(errorMessages.toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to update the user: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteUser(Long userId) {
        try {
            if (!userRepository.existsById(userId)) {
                return false;
            }
            userRepository.deleteById(userId);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete user because he is still referenced by some order.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user by ID: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return Optional.ofNullable(userRepository.findByEmail(email));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user by email: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> findById(Long userId) {
        try {
            return userRepository.findById(userId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user by ID: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return Optional.ofNullable(userRepository.findByUsername(username));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user by username: " + e.getMessage());
        }
    }

    @Override
    public User loginUser(LoginRequest request) {
        try {
            Optional<User> retrievedUser = Optional.ofNullable(userRepository.findByUsername(request.getUsername()));

            if (retrievedUser.isPresent()) {
                if (request.getPassword().equals(retrievedUser.get().getPassword())) {
                    return retrievedUser.get();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to log in the user: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteUserByUsername(String username) {
        try {
            User retrievedUser = userRepository.findByUsername(username);
            if (retrievedUser == null) {
                return false;
            }
            userRepository.delete(retrievedUser);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete user because he is still referenced by some order.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user by username: " + e.getMessage());
        }
    }
}
