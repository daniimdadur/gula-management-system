package com.guvaren.order.master.repository;

import com.guvaren.order.master.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
}

