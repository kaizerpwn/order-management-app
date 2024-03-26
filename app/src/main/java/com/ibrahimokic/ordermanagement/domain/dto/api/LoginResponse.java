package com.ibrahimokic.ordermanagement.domain.dto.api;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private final String accessToken;
}
