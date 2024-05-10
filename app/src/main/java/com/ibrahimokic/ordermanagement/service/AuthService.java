package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.domain.dto.api.LoginRequest;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    public ResponseEntity<?> loginUser(@Validated @RequestBody LoginRequest request, HttpServletResponse response);
    public ResponseEntity<User> registerUser(@RequestBody @Valid UserDto userDto, HttpServletResponse response);
}
