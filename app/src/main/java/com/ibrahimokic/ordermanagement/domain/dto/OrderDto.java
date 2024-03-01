package com.ibrahimokic.ordermanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibrahimokic.ordermanagement.controller.dto.CreateOrderItemDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    @JsonProperty("user_id")
    @NotNull
    private Long userId;

    @JsonProperty("order_date")
    private LocalDate orderDate;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("delivery_address_id")
    private AddressDto deliveryAddress;

    @JsonProperty("source_address_id")
    private AddressDto sourceAddress;

    @JsonProperty("items")
    @Valid
    private List<CreateOrderItemDto> orderItems = new ArrayList<>();
}

