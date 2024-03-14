package com.ibrahimokic.ordermanagement.utils;

import com.ibrahimokic.ordermanagement.domain.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public class Utils {
    public static BigDecimal calculateTotalProductsPriceAmount(List<OrderItem> orderItems) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            totalAmount = totalAmount.add(orderItem.getProduct().getPrice());
        }
        return totalAmount;
    }
}
