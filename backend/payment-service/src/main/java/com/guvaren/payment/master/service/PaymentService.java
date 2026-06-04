package com.guvaren.payment.master.service;

import com.guvaren.payment.master.dto.ConfirmPaymentReq;
import com.guvaren.payment.master.dto.PaymentReq;
import com.guvaren.payment.master.dto.PaymentRes;
import java.util.Optional;

public interface PaymentService {
    Optional<PaymentRes> create(PaymentReq request);
    Optional<PaymentRes> confirm(String id, ConfirmPaymentReq request);
    Optional<PaymentRes> getByOrderId(String orderId);
}

