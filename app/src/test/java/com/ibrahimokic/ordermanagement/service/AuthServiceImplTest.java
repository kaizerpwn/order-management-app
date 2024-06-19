package com.ibrahimokic.ordermanagement.service;

import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.domain.dto.api.LoginRequest;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.mapper.Mapper;
import com.ibrahimokic.ordermanagement.security.JwtIssuer;
import com.ibrahimokic.ordermanagement.service.impl.AuthServiceImpl;
import org.hibernate.MappingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtIssuer jwtIssuer;

    @Mock
    private Mapper<User, UserDto> userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginUserSuccess() {
        LoginRequest loginRequest = LoginRequest.builder()
                .username("ibrahim")
                .password("123123")
                .build();

        HttpServletResponse response = mock(HttpServletResponse.class);

        User user = User.builder()
                .userId(1L)
                .username("ibrahim")
                .password("123123")
                .role("USER")
                .build();

        when(userService.loginUser(loginRequest)).thenReturn(user);

        ResponseEntity<?> responseEntity = authService.loginUser(loginRequest, response);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());

        verify(jwtIssuer, times(1)).issue(anyLong(), eq("ibrahim"), eq("USER"));
        verify(response, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    void testLoginUserNotFound() {
        LoginRequest loginRequest = LoginRequest.builder()
                .username("username")
                .password("password")
                .build();
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(userService.loginUser(loginRequest)).thenReturn(null);

        ResponseEntity<?> responseEntity = authService.loginUser(loginRequest, response);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(MediaType.TEXT_PLAIN, responseEntity.getHeaders().getContentType());
        assertEquals("Username and password does not match any user in the database", responseEntity.getBody());

        verifyNoInteractions(jwtIssuer);
        verifyNoInteractions(response);
    }

    @Test
    void testRegisterUserSuccess() {
        UserDto userDto = UserDto.builder().build();
        HttpServletResponse response = mock(HttpServletResponse.class);
        User user = User.builder()
                .userId(1L)
                .username("ibrahim")
                .password("123123")
                .role("USER")
                .build();

        when(userMapper.mapFrom(userDto)).thenReturn(user);
        when(userService.createUser(user)).thenReturn(user);
        when(jwtIssuer.issue(anyLong(), anyString(), anyString())).thenReturn("accessToken");

        ResponseEntity<User> responseEntity = authService.registerUser(userDto, response);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());

        verify(jwtIssuer, times(1)).issue(anyLong(), anyString(), anyString());
        verify(response, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    void testRegisterUserBadRequest() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        ResponseEntity<User> responseEntity = authService.registerUser(null, response);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verifyNoInteractions(userMapper);
        verifyNoInteractions(userService);
        verifyNoInteractions(jwtIssuer);
        verifyNoInteractions(response);
    }

    @Test
    void testRegisterUserMappingException() {
        UserDto userDto = UserDto.builder().build();

        HttpServletResponse response = mock(HttpServletResponse.class);

        when(userMapper.mapFrom(userDto)).thenThrow(MappingException.class);

        ResponseEntity<User> responseEntity = authService.registerUser(userDto, response);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        verifyNoInteractions(userService);
        verifyNoInteractions(jwtIssuer);
        verifyNoInteractions(response);
    }

}
