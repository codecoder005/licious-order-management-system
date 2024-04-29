package com.licious.oms.service.impl;

import com.licious.oms.common.OrderStatus;
import com.licious.oms.dto.request.PlaceOrderRequest;
import com.licious.oms.dto.response.CancelOrderResponse;
import com.licious.oms.dto.response.OrderDto;
import com.licious.oms.dto.response.OrderStatusUpdateResponse;
import com.licious.oms.entity.ItemEntity;
import com.licious.oms.entity.OrderEntity;
import com.licious.oms.entity.OrderItemAssociationEntity;
import com.licious.oms.exception.AppException;
import com.licious.oms.exception.InSufficientItemsInStockException;
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

import java.math.BigDecimal;
import java.util.*;

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
    void shouldPlaceOrder() {
        UUID orderId = UUID.randomUUID();
        PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest();
        UUID itemId1 = UUID.randomUUID();
        Integer item1Quantity = 10;
        placeOrderRequest.setItems(Map.of(itemId1, item1Quantity));
        PlaceOrderRequest.Address deliveryAddress = new PlaceOrderRequest.Address();
        deliveryAddress.setAddressLine1("address line 1");
        deliveryAddress.setAddressLine2("address line 2");
        deliveryAddress.setCity("city");
        deliveryAddress.setState("state");
        deliveryAddress.setPinCode("123456");
        placeOrderRequest.setDeliveryAddress(deliveryAddress);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderId);
        orderEntity.setAmount(new BigDecimal("100.00"));
        orderEntity.setAddressLine1("address line 1");
        orderEntity.setAddressLine2("address line 2");
        orderEntity.setCity("city");
        orderEntity.setState("state");
        orderEntity.setPinCode("123456");

        when(modelMapper.map(any(PlaceOrderRequest.Address.class), eq(OrderEntity.class)))
                .thenReturn(orderEntity);
        when(itemRepository.findById(any())).thenReturn(Optional.of(
                ItemEntity.builder()
                        .id(itemId1)
                        .price(new BigDecimal("10.00"))
                        .availableStockUnits(100L)
                        .build()
        ));

        when(orderRepository.saveAndFlush(any())).thenReturn(orderEntity);

        List<OrderItemAssociationEntity> associationEntities = new ArrayList<>();
        associationEntities.add(
                OrderItemAssociationEntity.builder()
                        .orderId(orderEntity.getId())
                        .itemId(itemId1)
                        .quantity(item1Quantity)
                        .build()
        );

        OrderDto finalOrderDto = new OrderDto();
        finalOrderDto.setId(orderId);
        finalOrderDto.setStatus(OrderStatus.PLACED);
        finalOrderDto.setAmount(orderEntity.getAmount());
        finalOrderDto.setOrderItems(new HashSet<>(associationEntities));
        finalOrderDto.setAddressLine1("address line 1");
        finalOrderDto.setAddressLine2("address line 2");
        finalOrderDto.setCity("city");
        finalOrderDto.setState("state");
        finalOrderDto.setPinCode("123456");

        when(orderItemAssociationRepository.saveAllAndFlush(any())).thenReturn(associationEntities);
        when(modelMapper.map(any(OrderEntity.class), eq(OrderDto.class))).thenReturn(finalOrderDto);

        OrderDto placeOrderResponseDto = orderService.placeOrder(placeOrderRequest);
    }

    @Test
    void placeOrderShouldThrowInsufficientStockException() {
        UUID orderId = UUID.randomUUID();
        PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest();
        UUID itemId1 = UUID.randomUUID();
        Integer item1Quantity = 200;
        placeOrderRequest.setItems(Map.of(itemId1, item1Quantity));

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderId);
        orderEntity.setAmount(new BigDecimal("100.00"));
        orderEntity.setAddressLine1("address line 1");
        orderEntity.setAddressLine2("address line 2");
        orderEntity.setCity("city");
        orderEntity.setState("state");
        orderEntity.setPinCode("123456");

        when(modelMapper.map(any(PlaceOrderRequest.Address.class), eq(OrderEntity.class)))
                .thenReturn(orderEntity);
        when(itemRepository.findById(any())).thenReturn(Optional.of(
                ItemEntity.builder()
                        .id(itemId1)
                        .availableStockUnits(100L)
                        .build()
        ));

        assertThrows(InSufficientItemsInStockException.class, () -> {
            orderService.placeOrder(placeOrderRequest);
        });
    }

    @Test
    void placeOrderShouldThrowEntityNotFoundExceptionForInvalidItems() {
        UUID orderId = UUID.randomUUID();
        PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest();
        UUID itemId1 = UUID.randomUUID();
        Integer item1Quantity = 200;
        placeOrderRequest.setItems(Map.of(itemId1, item1Quantity));

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderId);
        orderEntity.setAmount(new BigDecimal("100.00"));
        orderEntity.setAddressLine1("address line 1");
        orderEntity.setAddressLine2("address line 2");
        orderEntity.setCity("city");
        orderEntity.setState("state");
        orderEntity.setPinCode("123456");

        when(modelMapper.map(any(PlaceOrderRequest.Address.class), eq(OrderEntity.class)))
                .thenReturn(orderEntity);
        when(itemRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            orderService.placeOrder(placeOrderRequest);
        });
    }

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

    @Test
    void shouldCancelTheOrderByOrderId() {
        UUID orderId = UUID.randomUUID();
        UUID itemId1 = UUID.randomUUID();
        UUID orderItemAssociationIde = UUID.randomUUID();

        OrderEntity orderEntity1 = new OrderEntity();
        orderEntity1.setId(orderId);
        orderEntity1.setStatus(OrderStatus.PLACED);

        orderEntity1.setOrderItems(new HashSet<>(
                Arrays.asList(
                        OrderItemAssociationEntity.builder()
                                .id(orderItemAssociationIde)
                                .orderId(orderId)
                                .itemId(itemId1)
                                .quantity(5)
                                .build()
                )
        ));

        ItemEntity itemEntity1 = new ItemEntity();
        itemEntity1.setId(itemId1);
        itemEntity1.setAvailableStockUnits(10L);

        when(orderRepository.findById(any())).thenReturn(Optional.of(orderEntity1));
        when(orderRepository.save(any())).thenReturn(new OrderEntity());
        when(itemRepository.findById(any())).thenReturn(Optional.of(itemEntity1));

        CancelOrderResponse cancelOrderResponse = orderService.cancelOrder(orderId);
    }

    @Test
    void cancellingACancelledOrderShouldThrowAppException() {
        UUID orderId = UUID.randomUUID();

        OrderEntity orderEntity1 = new OrderEntity();
        orderEntity1.setId(orderId);
        orderEntity1.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(any())).thenReturn(Optional.of(orderEntity1));
        when(orderRepository.save(any())).thenReturn(new OrderEntity());

        assertThrows(AppException.class, () -> {
            orderService.cancelOrder(orderId);
        });
    }

    @Test
    void cancellingAnInvalidOrderShouldThrowEntityNotFoundException() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.cancelOrder(orderId);
        });
    }
}