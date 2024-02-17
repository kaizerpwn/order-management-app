package com.ibrahimokic.ordermanagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "available_from", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate availableFrom;

    @Column(name = "available_until", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate availableUntil;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;
}
