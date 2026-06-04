package com.guvaren.product.master.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductListRes {
    private List<ProductRes> data;
    private Integer page;
    private Integer size;
    private Long total;
}

