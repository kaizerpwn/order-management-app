package com.ibrahimokic.ordermanagement.service.impl;

import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.domain.dto.api.LoginRequest;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.mapper.Mapper;
import com.ibrahimokic.ordermanagement.security.JwtIssuer;
import com.ibrahimokic.ordermanagement.service.AuthService;
import com.ibrahimokic.ordermanagement.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.MappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtIssuer jwtIssuer;
    private final Mapper<User, UserDto> userMapper;

    @Override
    public ResponseEntity<?> loginUser(LoginRequest request, HttpServletResponse response) {
        User user = userService.loginUser(request);

        if (user != null) {
            issueNewJwt(response, user, request.getUsername());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Successfully logged in");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Username and password does not match any user in the database");
        }
    }

    @Override
    public ResponseEntity<User> registerUser(UserDto userDto, HttpServletResponse response) {
        try {
            if (userDto == null) {
                return ResponseEntity.badRequest().build();
            }


            User user = userMapper.mapFrom(userDto);
            User createdUser = userService.createUser(user);

            issueNewJwt(response, user, user.getUsername());

            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (MappingException mappingException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void issueNewJwt(HttpServletResponse response, User user, String username) {
        String accessToken = jwtIssuer.issue(
                user.getUserId(),
                username,
                user.getRole()
        );

        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
