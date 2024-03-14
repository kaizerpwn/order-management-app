package com.ibrahimokic.ordermanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    @JsonProperty("user_id")
    @NotNull
    private Long userId;

    @JsonProperty("order_date")
    private LocalDate orderDate;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("delivery_address")
    private AddressDto deliveryAddress;

    @JsonProperty("source_address")
    private AddressDto sourceAddress;

    @JsonProperty("items")
    @Valid
    private List<OrderItemDto> orderItems = new ArrayList<>();
}
