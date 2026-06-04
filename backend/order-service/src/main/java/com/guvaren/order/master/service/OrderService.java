package com.guvaren.order.master.service;

import com.guvaren.order.master.dto.CreateOrderReq;
import com.guvaren.order.master.dto.OrderRes;
import com.guvaren.order.master.dto.OrderStatusReq;
import java.util.Optional;

public interface OrderService {
    Optional<OrderRes> create(CreateOrderReq request);
    Optional<OrderRes> getById(String id);
    Optional<OrderRes> updateStatus(String id, OrderStatusReq request);
}

