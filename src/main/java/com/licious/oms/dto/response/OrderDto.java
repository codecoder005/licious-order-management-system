package com.licious.oms.dto.response;

import com.licious.oms.common.OrderStatus;
import com.licious.oms.entity.OrderItemAssociationEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderDto {
    private UUID id;
    private BigDecimal amount;
    private OrderStatus status;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pinCode;
    private String phoneNumber;
    private String email;

    private Set<OrderItemAssociationEntity> orderItems;
}
