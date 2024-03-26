package com.ibrahimokic.ordermanagement.domain.dto.api;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {
    private String username;
    private String password;
}
