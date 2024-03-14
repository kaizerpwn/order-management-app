package com.ibrahimokic.ordermanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    @JsonProperty("product_id")
    private Long productId;

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
