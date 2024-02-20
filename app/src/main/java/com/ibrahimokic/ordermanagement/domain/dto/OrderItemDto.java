package com.ibrahimokic.ordermanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    @JsonProperty("order_id")
    @NotNull
    private Long orderId;

    @JsonProperty("product_id")
    @NotNull
    private Long productId;

    @JsonProperty("quantity")
    @NotNull
    private Integer quantity;

    @JsonProperty("item_price")
    @NotNull
    private BigDecimal itemPrice;
}

