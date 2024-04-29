package com.licious.oms.entity;

import com.licious.oms.common.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity{

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;


    @Column(nullable = false)
    private String addressLine1;
    private String addressLine2;

    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String state;
    @Column(nullable = false)
    private String pinCode;
    @Column(nullable = false)
    private String phoneNumber;
    private String email;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<OrderItemAssociationEntity> orderItems;
}
