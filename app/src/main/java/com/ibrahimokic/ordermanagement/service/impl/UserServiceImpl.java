package com.ibrahimokic.ordermanagement.service.impl;

import com.ibrahimokic.ordermanagement.repository.UserRepository;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.service.UserService;
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
    public Optional<User> getUserById(Long userId){
        return userRepository.findById(userId);
    }

    @Override
    public User createUser(User user){
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long userId, User newUser){
        if(userRepository.existsById(userId)){
            newUser.setUserId(userId);
            return userRepository.save(newUser);
        }
        else {
            return null;
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}