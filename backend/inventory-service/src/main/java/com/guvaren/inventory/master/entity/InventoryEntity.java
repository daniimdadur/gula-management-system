package com.guvaren.inventory.master.entity;

import com.guvaren.inventory.base.BaseAuditableSoftDelete;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE t_inventory SET deleted_at=CURRENT_TIMESTAMP WHERE id=?")
@Table(name = "t_inventory")
public class InventoryEntity extends BaseAuditableSoftDelete {

    @Id
    @Column(name = "id", nullable = false, length = 32)
    private String id;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "current_stock")
    private Integer currentStock;

    @Column(name = "minimum_stock")
    private Integer minimumStock;

    @OneToMany(mappedBy = "inventory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<InventoryTransactionEntity> inventoryTransactions;

}

