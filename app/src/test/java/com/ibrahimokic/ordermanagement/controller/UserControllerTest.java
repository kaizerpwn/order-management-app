package com.ibrahimokic.ordermanagement.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private MockMvc api;

    @Test
    void testNotLoggedIn_shouldNotSeeSecuredEndpoint() throws Exception {
        api.perform(get("/api/users/orders"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void testLoggedIn_shouldSeeSecuredEndpoint() throws Exception {
        api.perform(get("/api/product"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testBasicUser_shouldNotSeeAdminEndpoint() throws Exception {
        api.perform(get("/api/orders"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testAdmin_shouldSeeAdminEndpoint() throws Exception {
        api.perform(get("/api/orders"))
                .andExpect(status().isOk());
    }
}
