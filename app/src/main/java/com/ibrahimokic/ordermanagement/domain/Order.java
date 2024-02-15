package com.ibrahimokic.ordermanagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "order_date", nullable = false)
    private Date orderDate;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_address_id", referencedColumnName = "address_id")
    private Address deliveryAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "source_address_id", referencedColumnName = "address_id")
    private Address sourceAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
}