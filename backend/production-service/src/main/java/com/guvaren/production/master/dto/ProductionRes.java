package com.guvaren.production.master.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductionRes {
    private String id;
    private String productId;
    private Integer quantity;
    private LocalDate productionDate;
    private String notes;
}

