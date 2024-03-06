package com.ibrahimokic.ordermanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @JsonProperty("user_id")
    @JsonIgnore
    private Long userId;

    @JsonProperty("username")
    @NotNull
    private String username;

    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String password;

    @JsonProperty("email")
    @NotNull
    private String email;

    @JsonProperty("role")
    @NotNull
    private String role;

    @JsonProperty("first_name")
    @NotNull
    private String firstName;

    @JsonProperty("last_name")
    @NotNull
    private String lastName;

    @JsonProperty("birth_date")
    @NotNull
    private LocalDate birthDate;

    @JsonProperty("address")
    @NotNull
    private AddressDto address;
}
