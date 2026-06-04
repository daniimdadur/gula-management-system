package com.guvaren.inventory.master.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryTransactionReq {
    private Integer quantity;
    private String referenceType;
    private String referenceId;
    private String notes;
}

