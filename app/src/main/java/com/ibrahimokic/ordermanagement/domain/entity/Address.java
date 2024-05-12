package com.ibrahimokic.ordermanagement.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "street")
    @NotNull
    @NotBlank(message = "Street is required")
    private String street;

    @Column(name = "zip")
    @NotNull
    @NotBlank(message = "Zip is required")
    private String zip;

    @Column(name = "city")
    @NotNull
    @NotBlank(message = "City is required")
    private String city;

    @Column(name = "country")
    @NotNull
    @NotBlank(message = "Country is required")
    private String country;

}
