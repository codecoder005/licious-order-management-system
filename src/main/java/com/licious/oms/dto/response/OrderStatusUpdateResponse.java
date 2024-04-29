package com.licious.oms.dto.response;

import com.licious.oms.common.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderStatusUpdateResponse {
    private UUID orderId;
    private OrderStatus oldStatus;
    private OrderStatus newStatus;
    private LocalDateTime updatedOn;
}
