package com.ibrahimokic.ordermanagement.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "order_items")
public class OrderItem {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "order_item_id")
        private Long orderItemId;

        @ManyToOne
        @JoinColumn(name = "order_id", nullable = false)
        private Order order;

        @ManyToOne(cascade = CascadeType.REMOVE)
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;

        @Column(nullable = false)
        @NotNull
        private int quantity;

        @Column(name = "item_price", nullable = false, precision = 10, scale = 2)
        @NotNull
        private BigDecimal itemPrice;
}
