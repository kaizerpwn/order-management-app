package com.ibrahimokic.ordermanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressDto {
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
