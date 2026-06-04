package com.guvaren.production.master.entity;

import com.guvaren.production.base.BaseAuditableSoftDelete;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE t_production SET deleted_at=CURRENT_TIMESTAMP WHERE id=?")
@Table(name = "t_production")
public class ProductionEntity extends BaseAuditableSoftDelete {

    @Id
    @Column(name = "id", nullable = false, length = 32)
    private String id;

    @Column(name = "product_id", length = 32)
    private String productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "production_date")
    private LocalDate productionDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

}

