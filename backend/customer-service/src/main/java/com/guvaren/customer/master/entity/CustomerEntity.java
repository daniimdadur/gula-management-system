package com.guvaren.customer.master.entity;

import com.guvaren.customer.base.BaseAuditableSoftDelete;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE t_customer SET deleted_at=CURRENT_TIMESTAMP WHERE id=?")
@Table(name = "t_customer")
public class CustomerEntity extends BaseAuditableSoftDelete {

    @Id
    @Column(name = "id", nullable = false, length = 32)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

}
