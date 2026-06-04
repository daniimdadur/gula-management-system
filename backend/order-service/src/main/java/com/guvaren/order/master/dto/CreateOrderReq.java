package com.guvaren.order.master.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderReq {
    private String customerId;
    private List<OrderItemReq> items;
}

