package com.guvaren.inventory.master.entity;

import com.guvaren.inventory.base.BaseAuditableSoftDelete;
import com.guvaren.inventory.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE t_inventory_transaction SET deleted_at=CURRENT_TIMESTAMP WHERE id=?")
@Table(name = "t_inventory_transaction")
public class InventoryTransactionEntity extends BaseAuditableSoftDelete {

    @Id
    @Column(name = "id", nullable = false, length = 32)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private InventoryEntity inventory;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private Integer quantity;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "reference_id", length = 32)
    private String referenceId;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

}

