package com.guvaren.product.master.repository;

import com.guvaren.product.master.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    @Query("SELECT p FROM ProductEntity p WHERE " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(p.code) LIKE LOWER(CONCAT('%', :q, '%'))) " +
           "AND (:category IS NULL OR p.category = :category)")
    Page<ProductEntity> search(@Param("q") String q, @Param("category") String category, Pageable pageable);
}

