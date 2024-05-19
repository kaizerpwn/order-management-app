package com.ibrahimokic.ordermanagement.repository;

import com.ibrahimokic.ordermanagement.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
