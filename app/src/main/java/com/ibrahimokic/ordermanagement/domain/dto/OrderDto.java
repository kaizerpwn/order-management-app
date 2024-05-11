package com.ibrahimokic.ordermanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("delivery_address")
    @NotNull
    private AddressDto deliveryAddress;

    @JsonProperty("source_address")
    @NotNull
    private AddressDto sourceAddress;

    @JsonProperty("items")
    @Valid
    private List<OrderItemDto> orderItems = new ArrayList<>();
}
