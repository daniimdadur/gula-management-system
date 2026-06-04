package com.guvaren.order.master.entity;

import com.guvaren.order.base.BaseAuditableSoftDelete;
import com.guvaren.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE t_order SET deleted_at=CURRENT_TIMESTAMP WHERE id=?")
@Table(name = "t_order")
public class OrderEntity extends BaseAuditableSoftDelete {

    @Id
    @Column(name = "id", nullable = false, length = 32)
    private String id;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItemEntity> orderItems;

}

