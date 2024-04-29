package com.licious.oms.service.impl;

import com.licious.oms.common.OrderStatus;
import com.licious.oms.dto.response.OrderDto;
import com.licious.oms.dto.response.OrderStatusUpdateResponse;
import com.licious.oms.entity.OrderEntity;
import com.licious.oms.exception.AppException;
import com.licious.oms.repository.ItemRepository;
import com.licious.oms.repository.OrderItemAssociationRepository;
import com.licious.oms.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles(profiles = {"unit-test"})
class OrderServiceImplTest {
    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private OrderItemAssociationRepository orderItemAssociationRepository;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void shouldReturnListOfOrders() {
        UUID orderId = UUID.randomUUID();
        OrderEntity orderEntity1 = new OrderEntity();
        orderEntity1.setId(orderId);

        List<OrderEntity> expectedOrders = new ArrayList<>();
        expectedOrders.add(orderEntity1);

        OrderDto orderDto1 = new OrderDto();
        orderDto1.setId(orderId);

        when(orderRepository.findAll()).thenReturn(expectedOrders);
        when(modelMapper.map(ArgumentMatchers.any(OrderEntity.class), eq(OrderDto.class))).thenReturn(orderDto1);

        List<OrderDto> responseOrders = orderService.getAllOrders();

        assertEquals(expectedOrders.size(), responseOrders.size());
        assertEquals(expectedOrders.get(0).getId(), responseOrders.get(0).getId());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnOrderById() {
        UUID orderId = UUID.randomUUID();

        OrderEntity orderEntity1 = new OrderEntity();
        orderEntity1.setId(orderId);

        OrderDto orderDto1 = new OrderDto();
        orderDto1.setId(orderId);

        when(orderRepository.findById(Mockito.any())).thenReturn(Optional.of(orderEntity1));
        when(modelMapper.map(ArgumentMatchers.any(OrderEntity.class), eq(OrderDto.class))).thenReturn(orderDto1);

        OrderDto responseOrderDto = orderService.getOrderById(orderId);

        assertNotNull(responseOrderDto);
        assertEquals(orderDto1.getId(), responseOrderDto.getId());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            orderService.getOrderById(orderId);
        });
    }

    @Test
    void shouldUpdateOrderStatus() {
        UUID orderId = UUID.randomUUID();
        OrderEntity orderEntity1 = new OrderEntity();
        orderEntity1.setId(orderId);
        orderEntity1.setStatus(OrderStatus.PLACED);

        when(orderRepository.findById(any())).thenReturn(Optional.of(orderEntity1));

        OrderStatusUpdateResponse actualResponse = orderService.updateOrderStatus(orderId, OrderStatus.PROCESSING);

        assertNotNull(actualResponse);
        assertEquals(orderId, actualResponse.getOrderId());
        assertEquals(OrderStatus.PLACED, actualResponse.getOldStatus());
        assertEquals(OrderStatus.PROCESSING, actualResponse.getNewStatus());
    }

    @Test
    void shouldThrowAppExceptionWhenReducingOrderStatus() {
        UUID orderId = UUID.randomUUID();
        OrderEntity orderEntity1 = new OrderEntity();
        orderEntity1.setId(orderId);
        orderEntity1.setStatus(OrderStatus.SHIPPED);

        when(orderRepository.findById(any())).thenReturn(Optional.of(orderEntity1));

        assertThrows(AppException.class, () -> {
            orderService.updateOrderStatus(orderId, OrderStatus.PROCESSING);
        });
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenOrderNotFoundWhileUpdatingOrderStatus() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED);
        });
    }
}