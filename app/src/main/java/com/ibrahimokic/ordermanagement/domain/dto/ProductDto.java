package com.ibrahimokic.ordermanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductDto {
    @JsonProperty("product_name")
    @NotNull
    private String productName;

    @JsonProperty("price")
    @NotNull
    private BigDecimal price;

    @JsonProperty("available_from")
    @Temporal(TemporalType.DATE)
    @NotNull
    private LocalDate availableFrom;

    @JsonProperty("available_until")
    @Temporal(TemporalType.DATE)
    @NotNull
    private LocalDate availableUntil;

    @JsonProperty("available_quantity")
    private int availableQuantity;
}
