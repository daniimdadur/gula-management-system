package com.guvaren.inventory.master.service;

import com.guvaren.inventory.master.dto.InventoryRes;
import com.guvaren.inventory.master.dto.InventoryTransactionReq;
import com.guvaren.inventory.master.dto.InventoryTransactionRes;
import java.util.List;
import java.util.Optional;

public interface InventoryService {
    Optional<InventoryRes> getByProductId(String productId);
    Optional<InventoryRes> stockIn(String inventoryId, InventoryTransactionReq request);
    Optional<InventoryRes> stockOut(String inventoryId, InventoryTransactionReq request);
    Optional<InventoryRes> adjust(String inventoryId, InventoryTransactionReq request);
    List<InventoryTransactionRes> getTransactionHistory(String inventoryId);
}

