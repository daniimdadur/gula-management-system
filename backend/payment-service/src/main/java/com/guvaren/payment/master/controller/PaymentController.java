package com.guvaren.payment.master.controller;

import com.guvaren.payment.base.BaseController;
import com.guvaren.payment.base.Response;
import com.guvaren.payment.master.dto.ConfirmPaymentReq;
import com.guvaren.payment.master.dto.PaymentReq;
import com.guvaren.payment.master.dto.PaymentRes;
import com.guvaren.payment.master.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController extends BaseController<PaymentRes> {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody PaymentReq request) {
        return super.getResponse(paymentService.create(request));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Response> confirm(@PathVariable String id, @RequestBody ConfirmPaymentReq request) {
        return super.getResponse(paymentService.confirm(id, request));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Response> getByOrderId(@PathVariable String orderId) {
        return super.getResponse(paymentService.getByOrderId(orderId));
    }
}

