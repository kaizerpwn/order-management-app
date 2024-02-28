package com.ibrahimokic.ordermanagement.repository;

import com.ibrahimokic.ordermanagement.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testGetUserById() {
        User userToSave = new User();
        userToSave.setUsername("testUser");
        userToSave.setEmail("test@gmail.com");
        userToSave.setPassword("123123");
        userToSave.setRole("user");

        User savedUser = userRepository.save(userToSave);

        Optional<User> retrievedUser = userRepository.findById(savedUser.getUserId());

        assertTrue(retrievedUser.isPresent());
        assertEquals(savedUser.getUserId(), retrievedUser.get().getUserId());
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setRole("user");
        user1.setPassword("pass2");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setRole("user");
        user2.setPassword("pass2");
        userRepository.save(user2);

        List<User> allUsers = userRepository.findAll();

        assertEquals(2, allUsers.size());
        assertTrue(allUsers.stream().anyMatch(user -> user.getUsername().equals("user1")));
        assertTrue(allUsers.stream().anyMatch(user -> user.getUsername().equals("user2")));
    }

    @Test
    void testDeleteUser(){
        User userUnderTest = new User();
        userUnderTest.setUsername("johndoe");
        userUnderTest.setRole("user");
        userUnderTest.setEmail("johndoe@gmail.com");
        userUnderTest.setPassword("123123");

        userRepository.save(userUnderTest);

        userRepository.deleteById(userUnderTest.getUserId());

        List<User> allUsers = userRepository.findAll();

        assertEquals(0, allUsers.size());
    }
}