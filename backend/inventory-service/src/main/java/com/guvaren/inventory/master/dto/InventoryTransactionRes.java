package com.guvaren.inventory.master.dto;

import com.guvaren.inventory.enums.TransactionType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryTransactionRes {
    private String id;
    private String inventoryId;
    private TransactionType transactionType;
    private Integer quantity;
    private String referenceType;
    private String referenceId;
    private String notes;
}

