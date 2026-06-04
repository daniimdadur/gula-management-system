package com.guvaren.notification.master.entity;

import com.guvaren.notification.base.BaseAuditableSoftDelete;
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
@SQLDelete(sql = "UPDATE t_notification SET deleted_at=CURRENT_TIMESTAMP WHERE id=?")
@Table(name = "t_notification")
public class NotificationEntity extends BaseAuditableSoftDelete {

    @Id
    @Column(name = "id", nullable = false, length = 32)
    private String id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "recipient")
    private String recipient;

    @Column(name = "status")
    private String status;

}

