package com.guvaren.order.master.service.impl;

import com.guvaren.order.enums.OrderStatus;
import com.guvaren.order.exception.NotFoundException;
import com.guvaren.order.master.dto.CreateOrderReq;
import com.guvaren.order.master.dto.OrderItemReq;
import com.guvaren.order.master.dto.OrderItemRes;
import com.guvaren.order.master.dto.OrderRes;
import com.guvaren.order.master.dto.OrderStatusReq;
import com.guvaren.order.master.entity.OrderEntity;
import com.guvaren.order.master.entity.OrderItemEntity;
import com.guvaren.order.master.repository.OrderItemRepository;
import com.guvaren.order.master.repository.OrderRepository;
import com.guvaren.order.master.service.OrderService;
import com.guvaren.order.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Optional<OrderRes> create(CreateOrderReq request) {
        // Calculate total amount
        BigDecimal totalAmount = request.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order
        OrderEntity order = OrderEntity.builder()
                .id(CommonUtil.getUUID())
                .customerId(request.getCustomerId())
                .orderDate(LocalDateTime.now())
                .totalAmount(totalAmount)
                .status(OrderStatus.CREATED)
                .build();

        orderRepository.save(order);

        // Create order items
        for (OrderItemReq itemReq : request.getItems()) {
            BigDecimal subtotal = itemReq.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            OrderItemEntity item = OrderItemEntity.builder()
                    .id(CommonUtil.getUUID())
                    .order(order)
                    .productId(itemReq.getProductId())
                    .productName(itemReq.getProductName())
                    .price(itemReq.getPrice())
                    .quantity(itemReq.getQuantity())
                    .subtotal(subtotal)
                    .build();
            orderItemRepository.save(item);
        }

        return getById(order.getId());
    }

    @Override
    public Optional<OrderRes> getById(String id) {
        return orderRepository.findById(id).map(this::toResponse);
    }

    @Override
    public Optional<OrderRes> updateStatus(String id, OrderStatusReq request) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id));

        order.setStatus(request.getStatus());
        orderRepository.save(order);
        return getById(id);
    }

    private OrderRes toResponse(OrderEntity entity) {
        List<OrderItemEntity> items = orderItemRepository.findByOrderId(entity.getId());
        List<OrderItemRes> itemResponses = items.stream().map(this::toItemResponse).toList();

        return OrderRes.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .orderDate(entity.getOrderDate())
                .totalAmount(entity.getTotalAmount())
                .status(entity.getStatus())
                .items(itemResponses)
                .build();
    }

    private OrderItemRes toItemResponse(OrderItemEntity entity) {
        return OrderItemRes.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .subtotal(entity.getSubtotal())
                .build();
    }
}

