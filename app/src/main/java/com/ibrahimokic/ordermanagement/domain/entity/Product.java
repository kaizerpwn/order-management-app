package com.ibrahimokic.ordermanagement.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name", nullable = false)
    @NotBlank(message = "Product name is required")
    @NotNull
    private String productName;

    @Column(nullable = false)
    @NotNull
    private BigDecimal price;

    @Column(name = "available_from", nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate availableFrom;

    @Column(name = "available_until", nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate availableUntil;

    @Column(name = "available_quantity", nullable = false)
    @NotNull
    private int availableQuantity;
}
