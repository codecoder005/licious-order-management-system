package com.licious.oms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders_items_association")
@EqualsAndHashCode(exclude = {"orderId", "itemId", "quantity"})
public class OrderItemAssociationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID orderId;
    private UUID itemId;
    private Integer quantity;
}
