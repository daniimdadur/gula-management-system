package com.guvaren.production.master.repository;

import com.guvaren.production.master.entity.ProductionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionRepository extends JpaRepository<ProductionEntity, String> {
}

