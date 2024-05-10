package com.ibrahimokic.ordermanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    @JsonProperty("address_id")
    private Long addressId;

    @JsonProperty("street")
    @NotNull
    @NotBlank
    private String street;

    @JsonProperty("zip_code")
    @NotNull
    @NotBlank
    private String zip;

    @JsonProperty("city")
    @NotNull
    @NotBlank
    private String city;

    @JsonProperty("country")
    @NotNull
    @NotBlank
    private String country;
}
