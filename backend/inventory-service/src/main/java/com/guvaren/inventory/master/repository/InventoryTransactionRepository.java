package com.guvaren.inventory.master.repository;

import com.guvaren.inventory.master.entity.InventoryTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransactionEntity, String> {
    List<InventoryTransactionEntity> findByInventoryId(String inventoryId);
}

