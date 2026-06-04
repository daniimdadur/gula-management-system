package com.guvaren.notification.master.repository;

import com.guvaren.notification.master.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {
}

