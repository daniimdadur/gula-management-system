package com.guvaren.production.master.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductionReq {
    private String productId;
    private Integer quantity;
    private LocalDate productionDate;
    private String notes;
}

