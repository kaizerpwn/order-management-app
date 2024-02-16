package com.ibrahimokic.ordermanagement.controller;

import com.ibrahimokic.ordermanagement.domain.User;
import com.ibrahimokic.ordermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Operations related to users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get a user by ID", description = "Get a user by providing its ID")
    @ApiResponse(responseCode = "200", description = "User found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
    })
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user)
    {
        return userService.createUser(user);
    }

    @PatchMapping@ResponseStatus(HttpStatus.OK) User updateUser(@PathVariable Long userId, @RequestBody User newUser)
    {
        return userService.updateUser(userId, newUser);
    }

    @DeleteMapping("/{userId}") @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
    }
}
