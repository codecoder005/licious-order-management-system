package com.licious.oms.controller;

import com.licious.oms.common.OrderStatus;
import com.licious.oms.dto.request.PlaceOrderRequest;
import com.licious.oms.dto.response.CancelOrderResponse;
import com.licious.oms.dto.response.OrderDto;
import com.licious.oms.dto.response.OrderStatusUpdateResponse;
import com.licious.oms.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<OrderDto> placeOrder(@Valid @RequestBody PlaceOrderRequest order) {
        log.info("OrderController::placeOrder");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.placeOrder(order));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        log.info("OrderController::getAllOrders");
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getAllOrders());
    }

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID id) {
        log.info("OrderController::getOrderById");
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrderById(id));
    }

    @PatchMapping(
            value = "/{orderId}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<OrderStatusUpdateResponse> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestParam(name = "status") OrderStatus status
    ) {
        log.info("OrderController::updateOrderStatus");
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.updateOrderStatus(orderId, status));
    }

    @PatchMapping(
            value = "/cancel/{orderId}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<CancelOrderResponse> cancelOrder(@PathVariable UUID orderId) {
        log.info("OrderController::cancelOrder");
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.cancelOrder(orderId));
    }
}
