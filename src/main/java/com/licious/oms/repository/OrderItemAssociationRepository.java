package com.licious.oms.repository;

import com.licious.oms.entity.OrderItemAssociationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemAssociationRepository extends JpaRepository<OrderItemAssociationEntity, UUID> {

}
