package com.licious.oms.service;

import com.licious.oms.common.OrderStatus;
import com.licious.oms.dto.request.PlaceOrderRequest;
import com.licious.oms.dto.response.CancelOrderResponse;
import com.licious.oms.dto.response.OrderDto;
import com.licious.oms.dto.response.OrderStatusUpdateResponse;
import com.licious.oms.exception.AppException;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    /**
     * Places a new order based on the provided order request.
     *
     * @param placeOrderRequest The request containing details of the order to be placed.
     * @return The OrderDto object representing the newly placed order.
     */
    OrderDto placeOrder(PlaceOrderRequest placeOrderRequest);

    /**
     * Retrieves a list of all orders.
     *
     * @return A list of OrderDto objects representing all orders.
     */
    List<OrderDto> getAllOrders();

    /**
     * Retrieves an order by its unique identifier.
     *
     * @param id The unique identifier of the order to retrieve.
     * @return The OrderDto object representing the order with the specified ID.
     * @throws IllegalArgumentException If the provided ID is null.
     * @throws jakarta.persistence.EntityNotFoundException   If no order with the provided ID is found.
     */
    OrderDto getOrderById(UUID id);

    /**
     * Updates the status of an existing order with the provided status.
     *
     * @param orderId The unique identifier of the order to update.
     * @param status  The new status of the order.
     * @return An OrderStatusUpdateResponse indicating the result of the status update.
     * @throws IllegalArgumentException If the provided order ID or status is null.
     * @throws jakarta.persistence.EntityNotFoundException   If no order with the provided ID is found.
     */
    OrderStatusUpdateResponse updateOrderStatus(UUID orderId, OrderStatus status);

    /**
     * Cancels an existing order with the provided order ID.
     *
     * @param orderId The unique identifier of the order to cancel.
     * @return A CancelOrderResponse indicating the result of the order cancellation.
     * @throws IllegalArgumentException If the provided order ID is null.
     * @throws jakarta.persistence.EntityNotFoundException   If no order with the provided ID is found.
     * @throws AppException If the order has already been cancelled.
     */
    CancelOrderResponse cancelOrder(UUID orderId);
}
