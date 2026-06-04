package com.guvaren.inventory.master.repository;

import com.guvaren.inventory.master.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, String> {
    Optional<InventoryEntity> findByProductId(String productId);
}

