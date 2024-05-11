package com.ibrahimokic.ordermanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    @JsonProperty("user_id")
    @JsonIgnore
    private Long userId;

    @JsonProperty("username")
    @NotNull
    @NotBlank(message = "Username is required")
    private String username;

    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @NotBlank(message = "Password is required")
    @Size(
            min = 5,
            message = "The password '${validatedValue}' must be greater then {min}"
    )
    private String password;

    @JsonProperty("email")
    @NotNull
    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @JsonProperty("role")
    @NotNull
    private String role;

    @JsonProperty("first_name")
    @NotNull
    @NotBlank(message = "First name is required")
    private String firstName;

    @JsonProperty("last_name")
    @NotNull
    @NotBlank(message = "Last name is required")
    private String lastName;

    @JsonProperty("birth_date")
    @NotNull
    @Past(message = "Birth date must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @JsonProperty("address")
    @NotNull
    private AddressDto address;
}
