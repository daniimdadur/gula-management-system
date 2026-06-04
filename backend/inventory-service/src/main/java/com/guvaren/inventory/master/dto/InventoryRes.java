package com.guvaren.inventory.master.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryRes {
    private String id;
    private String productId;
    private Integer currentStock;
    private Integer minimumStock;
}

