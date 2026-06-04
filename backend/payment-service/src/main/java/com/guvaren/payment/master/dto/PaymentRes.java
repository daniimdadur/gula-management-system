package com.guvaren.payment.master.dto;

import com.guvaren.payment.enums.PaymentMethod;
import com.guvaren.payment.enums.PaymentStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRes {
    private String id;
    private String orderId;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
}

