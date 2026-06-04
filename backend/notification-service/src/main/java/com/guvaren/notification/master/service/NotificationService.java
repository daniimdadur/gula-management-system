package com.guvaren.notification.master.service;

import com.guvaren.notification.master.dto.NotificationReq;
import com.guvaren.notification.master.dto.NotificationRes;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    List<NotificationRes> getAll();
    Optional<NotificationRes> sendTestNotification(NotificationReq request);
}

