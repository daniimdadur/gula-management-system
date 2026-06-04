package com.guvaren.product.master.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReq {
    private String code;
    private String name;
    private String category;
    private String description;
    private BigDecimal weight;
    private BigDecimal price;
    private String imageUrl;
    private Boolean status;
}

