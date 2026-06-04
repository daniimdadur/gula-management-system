package com.guvaren.payment.master.dto;

import com.guvaren.payment.enums.PaymentStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmPaymentReq {
    private PaymentStatus status;
    private LocalDateTime paymentDate;
}

