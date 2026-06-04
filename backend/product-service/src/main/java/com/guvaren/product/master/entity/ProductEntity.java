package com.guvaren.product.master.entity;

import com.guvaren.product.base.BaseAuditableSoftDelete;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE t_product SET deleted_at=CURRENT_TIMESTAMP WHERE id=?")
@Table(name = "t_product")
public class ProductEntity extends BaseAuditableSoftDelete {

    @Id
    @Column(name = "id", nullable = false, length = 32)
    private String id;

    @Column(name = "code", length = 50, unique = true)
    private String code;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "status")
    private Boolean status;

}

