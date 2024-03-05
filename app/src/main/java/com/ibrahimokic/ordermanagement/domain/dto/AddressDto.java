package com.ibrahimokic.ordermanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    @JsonProperty("address_id")
    private Long addressId;

    @JsonProperty("street")
    @NotNull
    private String street;

    @JsonProperty("zip_code")
    @NotNull
    private String zip;

    @JsonProperty("city")
    @NotNull
    private String city;

    @JsonProperty("country")
    @NotNull
    private String country;
}
