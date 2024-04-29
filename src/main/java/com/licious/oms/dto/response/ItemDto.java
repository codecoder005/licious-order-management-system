package com.licious.oms.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ItemDto {
    private UUID id;
    private String name;
    private String description;
    private Integer weight;
    private BigDecimal price;
    private Long availableStockUnits;

    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
