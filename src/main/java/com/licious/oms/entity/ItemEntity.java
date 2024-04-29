package com.licious.oms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@Entity
@Table(name = "items")
public class ItemEntity extends BaseEntity{
    @Column(nullable = false)
    private String name;
    private String description;

    @Column(nullable = false)
    private Integer weight;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Long availableStockUnits;
}
