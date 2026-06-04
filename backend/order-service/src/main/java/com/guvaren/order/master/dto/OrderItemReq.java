package com.guvaren.order.master.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemReq {
    private String productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
}

