package com.ibrahimokic.ordermanagement.service.impl;

import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.domain.dto.api.LoginRequest;
import com.ibrahimokic.ordermanagement.repository.UserRepository;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> updateUser(Long userId, UserDto updatedUserDto) {
        Optional<User> optionalExistingUser = userRepository.findById(userId);

        if (optionalExistingUser.isPresent()) {
            User existingUser = optionalExistingUser.get();
            BeanUtils.copyProperties(updatedUserDto, existingUser);

            try {
                userRepository.save(existingUser);
                return Optional.of(existingUser);
            } catch (Exception e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteUser(Long userId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                userRepository.deleteById(userId);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    @Override
    public User loginUser(LoginRequest request) {
        Optional<User> retrievedUser = Optional.ofNullable(userRepository.findByUsername(request.getUsername()));

        if (retrievedUser.isPresent()) {
            if (request.getPassword().equals(retrievedUser.get().getPassword()))
                return retrievedUser.get();

            else
                return null;
        } else {
            return null;
        }
    }
}
