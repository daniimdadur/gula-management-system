package com.guvaren.payment.master.service.impl;

import com.guvaren.payment.enums.PaymentStatus;
import com.guvaren.payment.exception.NotFoundException;
import com.guvaren.payment.master.dto.ConfirmPaymentReq;
import com.guvaren.payment.master.dto.PaymentReq;
import com.guvaren.payment.master.dto.PaymentRes;
import com.guvaren.payment.master.entity.PaymentEntity;
import com.guvaren.payment.master.repository.PaymentRepository;
import com.guvaren.payment.master.service.PaymentService;
import com.guvaren.payment.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public Optional<PaymentRes> create(PaymentReq request) {
        PaymentEntity payment = PaymentEntity.builder()
                .id(CommonUtil.getUUID())
                .orderId(request.getOrderId())
                .paymentMethod(request.getPaymentMethod())
                .amount(request.getAmount())
                .status(PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);
        return Optional.of(toResponse(payment));
    }

    @Override
    public Optional<PaymentRes> confirm(String id, ConfirmPaymentReq request) {
        PaymentEntity payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + id));

        payment.setStatus(request.getStatus());
        payment.setPaymentDate(request.getPaymentDate());
        paymentRepository.save(payment);
        return Optional.of(toResponse(payment));
    }

    @Override
    public Optional<PaymentRes> getByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId).map(this::toResponse);
    }

    private PaymentRes toResponse(PaymentEntity entity) {
        return PaymentRes.builder()
                .id(entity.getId())
                .orderId(entity.getOrderId())
                .paymentMethod(entity.getPaymentMethod())
                .amount(entity.getAmount())
                .status(entity.getStatus())
                .paymentDate(entity.getPaymentDate())
                .build();
    }
}

