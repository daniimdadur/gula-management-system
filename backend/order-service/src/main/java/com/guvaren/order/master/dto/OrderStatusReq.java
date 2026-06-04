package com.guvaren.order.master.dto;

import com.guvaren.order.enums.OrderStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusReq {
    private OrderStatus status;
}

