package com.guvaren.order.master.controller;

import com.guvaren.order.base.BaseController;
import com.guvaren.order.base.Response;
import com.guvaren.order.master.dto.CreateOrderReq;
import com.guvaren.order.master.dto.OrderRes;
import com.guvaren.order.master.dto.OrderStatusReq;
import com.guvaren.order.master.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController extends BaseController<OrderRes> {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody CreateOrderReq request) {
        return super.getResponse(orderService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable String id) {
        return super.getResponse(orderService.getById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Response> updateStatus(@PathVariable String id, @RequestBody OrderStatusReq request) {
        return super.getResponse(orderService.updateStatus(id, request));
    }
}

