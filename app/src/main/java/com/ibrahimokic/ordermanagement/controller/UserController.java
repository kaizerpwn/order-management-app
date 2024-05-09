package com.ibrahimokic.ordermanagement.controller;

import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import com.ibrahimokic.ordermanagement.domain.dto.api.LoginRequest;
import com.ibrahimokic.ordermanagement.domain.entity.Order;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.mapper.impl.OrderMapperImpl;
import com.ibrahimokic.ordermanagement.service.AuthService;
import com.ibrahimokic.ordermanagement.service.OrderService;
import com.ibrahimokic.ordermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Operations related to users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final OrderService orderService;
    private final OrderMapperImpl orderMapper;

    @GetMapping
    @Operation(summary = "Get all users", description = "Get list of all users")
    @ApiResponse(responseCode = "200", description = "List of users", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))
    })
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}/orders")
    @Operation(summary = "Get orders of a specific user", description = "Get a list of orders belonging to a specific user")
    @ApiResponse(responseCode = "200", description = "List of orders belonging to the user", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))
    })
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable("userId") Long userId) {
        Optional<User> user = userService.getUserById(userId);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Order> userOrders = orderService.getAllUsersOrders(user.get());
        List<OrderDto> userOrdersDto = userOrders.stream()
                .map(orderMapper::mapTo)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userOrdersDto);
    }


    @GetMapping("/{userId}")
    @Operation(summary = "Get a user by ID", description = "Get a user by providing its ID")
    @ApiResponse(responseCode = "200", description = "User found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
    })
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        Optional<User> userOptional = userService.getUserById(userId);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok().body(userOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + userId + " not found.");
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new user", description = "Create new user based on request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<User> registerUser(@RequestBody(required = false) @Valid UserDto userDto, HttpServletResponse response) {
        return authService.registerUser(userDto, response);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new user", description = "Authenticate user with his username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully logged in", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "Username and password does not match any user in the database", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<?> loginUser(@Validated @RequestBody LoginRequest request, HttpServletResponse response) {
        return authService.loginUser(request, response);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Edit user", description = "Edit user based on request body and user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "An error occurred while deleting user")
    })
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserDto updatedUserDto) {
        Optional<User> updatedUser = userService.updateUser(userId, updatedUserDto);

        if (updatedUser.isPresent()) {
            return ResponseEntity.ok().body(updatedUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("An error occurred while deleting user " + userId + ".");
        }
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete user", description = "Delete user with provided user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while deleting user", content = @Content)
    })
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        if(userService.deleteUser(userId)) {
            return ResponseEntity.status(HttpStatus.OK).body("User with ID " + userId + " successfully deleted.");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("An error occurred while deleting user " + userId + ".");
        }
    }
}
