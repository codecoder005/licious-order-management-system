package com.licious.oms.common;

/**
 * OrderStatus = PLACED has the least precedence.
 * OrderStatus = REFUNDED has the highest precedence.
 * i.e, if an order has status of PLACED, it can be updated to PROCESSING,SHIPPED,DELIVERED,CANCELLED,REFUNDED
 * if an order has status of SHIPPED then it can not be updated to PLACED or PROCESSING
 */
public enum OrderStatus {
    PLACED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    REFUNDED
}
