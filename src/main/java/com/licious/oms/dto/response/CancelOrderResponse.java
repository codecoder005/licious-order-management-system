package com.licious.oms.dto.response;

import com.licious.oms.common.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CancelOrderResponse {
    private UUID orderId;
    private OrderStatus oldStatus;
    private OrderStatus currentStatus;
    private LocalDateTime cancelledOn;
    private String additionalMessage;
}
