package com.licious.oms.service.impl;

import com.licious.oms.common.OrderStatus;
import com.licious.oms.dto.request.PlaceOrderRequest;
import com.licious.oms.dto.response.CancelOrderResponse;
import com.licious.oms.dto.response.OrderDto;
import com.licious.oms.dto.response.OrderStatusUpdateResponse;
import com.licious.oms.entity.OrderEntity;
import com.licious.oms.entity.OrderItemAssociationEntity;
import com.licious.oms.exception.AppException;
import com.licious.oms.exception.InSufficientItemsInStockException;
import com.licious.oms.repository.ItemRepository;
import com.licious.oms.repository.OrderItemAssociationRepository;
import com.licious.oms.repository.OrderRepository;
import com.licious.oms.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderItemAssociationRepository orderItemAssociationRepository;
    private final ModelMapper modelMapper;

    @Transactional(rollbackOn = RuntimeException.class)
    public OrderDto placeOrder(PlaceOrderRequest placeOrderRequest) {
        log.info("OrderService::placeOrder: {}", placeOrderRequest);
        OrderEntity orderEntity = modelMapper.map(placeOrderRequest.getDeliveryAddress(), OrderEntity.class);
        AtomicReference<BigDecimal> cartValue = new AtomicReference<>(new BigDecimal("0.0"));

        placeOrderRequest.getItems().entrySet().stream()
                        .forEach(entryItem -> {
                            itemRepository.findById(entryItem.getKey()).ifPresentOrElse(
                                    (item) -> {
                                        long currentAvailableQuantity = item.getAvailableStockUnits();
                                        int desiredQuantity = entryItem.getValue();
                                        if(currentAvailableQuantity < desiredQuantity) {
                                            throw new InSufficientItemsInStockException("Insufficient items in stock");
                                        }
                                        item.setAvailableStockUnits(currentAvailableQuantity - desiredQuantity);
                                        cartValue.set(cartValue.get().add(item.getPrice().multiply(new BigDecimal(desiredQuantity))));
                                    },
                                    () -> {
                                        throw new EntityNotFoundException("no item found with id: " + entryItem.getKey());
                                    }
                            );
                        });


        List<OrderItemAssociationEntity> orderItemAssociationEntities = new ArrayList<>();

        orderEntity.setAmount(cartValue.get());
        orderEntity.setStatus(OrderStatus.PLACED);
        OrderEntity successfulOrder = orderRepository.saveAndFlush(orderEntity);
        placeOrderRequest.getItems().entrySet()
                .forEach(entryItem -> {
                    orderItemAssociationEntities.add(
                            OrderItemAssociationEntity.builder()
                                    .orderId(successfulOrder.getId())
                                    .itemId(entryItem.getKey())
                                    .quantity(entryItem.getValue())
                                    .build()
                );
        });
        List<OrderItemAssociationEntity> savedOrderItemAssociations = orderItemAssociationRepository.saveAllAndFlush(orderItemAssociationEntities);
        successfulOrder.setOrderItems(new HashSet<>(savedOrderItemAssociations));
        return modelMapper.map(successfulOrder, OrderDto.class);
    }

    public List<OrderDto> getAllOrders() {
        log.info("OrderService::getAllOrders");
        return orderRepository.findAll().stream().map(order -> modelMapper.map(order, OrderDto.class)).toList();
    }

    public OrderDto getOrderById(UUID id) {
        log.info("OrderService::getOrderById");
        return modelMapper.map(
                orderRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("no order found with id: " + id)),
                OrderDto.class
        );
    }

    public OrderStatusUpdateResponse updateOrderStatus(UUID orderId, OrderStatus status) {
        OrderStatusUpdateResponse response = new OrderStatusUpdateResponse();
        orderRepository.findById(orderId).ifPresentOrElse(
                (order) -> {
                    if(status.ordinal() <= order.getStatus().ordinal()) {
                        throw new AppException("we can not reduce order status");
                    }
                    response.setOrderId(orderId);
                    response.setOldStatus(order.getStatus());
                    response.setNewStatus(status);
                    order.setStatus(status);
                    orderRepository.save(order);
                },
                () -> {
                    throw new EntityNotFoundException("no order found with id: " + orderId);
                }
        );
        return response;
    }

    public CancelOrderResponse cancelOrder(UUID orderId) {
        log.info("OrderService::cancelOrder");
        CancelOrderResponse response = new CancelOrderResponse();
        orderRepository.findById(orderId).ifPresentOrElse(
                (order) -> {
                    if(order.getStatus() == OrderStatus.CANCELLED) {
                        throw new AppException("Your order had already been cancelled");
                    }
                    response.setOrderId(orderId);
                    response.setCancelledOn(LocalDateTime.now());
                    response.setOldStatus(order.getStatus());
                    response.setCurrentStatus(OrderStatus.CANCELLED);
                    response.setAdditionalMessage("Yey, Your order has been cancelled");

                    order.setStatus(OrderStatus.CANCELLED);
                    orderRepository.save(order);
                    order.getOrderItems().stream()
                            .forEach(orderItem -> {
                                itemRepository.findById(orderItem.getItemId()).ifPresent(
                                        (toBeUpdatedItem) -> {
                                            long finalQuantity = toBeUpdatedItem.getAvailableStockUnits() + orderItem.getQuantity();
                                            toBeUpdatedItem.setAvailableStockUnits(finalQuantity);
                                            itemRepository.save(toBeUpdatedItem);
                                        }
                                );
                            });
                },
                () -> {
                    throw new EntityNotFoundException("no order found with id: " + orderId);
                }
        );
        return response;
    }
}
