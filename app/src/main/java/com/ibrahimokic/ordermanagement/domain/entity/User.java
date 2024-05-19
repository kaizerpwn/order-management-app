package com.ibrahimokic.ordermanagement.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", unique = true, nullable = false)
    @NotBlank(message = "Username is required")
    private String username;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "The password '${validatedValue}' must be greater then {min}")
    private String password;

    @Column(name = "email", nullable = false)
    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "first_name")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Column(name = "birth_date")
    @NotNull
    @PastOrPresent(message = "Birth date must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    private Address address;

    public boolean checkUserPassword(String password) {
        return password.equals(this.password);
    }
}
