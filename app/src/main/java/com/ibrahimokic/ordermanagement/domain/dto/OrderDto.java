package com.ibrahimokic.ordermanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibrahimokic.ordermanagement.domain.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    @JsonProperty("user_id")
    @NotNull
    private Long userId;

    @JsonProperty("order_date")
    private LocalDate orderDate;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("delivery_address_id")
    private Address deliveryAddress;

    @JsonProperty("source_address_id")
    private Address sourceAddress;

    @JsonProperty("items")
    @Valid
    private List<OrderItemDto> orderItems = new ArrayList<>();
}

