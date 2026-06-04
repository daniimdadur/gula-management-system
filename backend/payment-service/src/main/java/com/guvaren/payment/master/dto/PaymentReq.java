package com.guvaren.payment.master.dto;

import com.guvaren.payment.enums.PaymentMethod;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentReq {
    private String orderId;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
}

