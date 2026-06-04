package com.guvaren.notification.master.service.impl;

import com.guvaren.notification.master.dto.NotificationReq;
import com.guvaren.notification.master.dto.NotificationRes;
import com.guvaren.notification.master.entity.NotificationEntity;
import com.guvaren.notification.master.repository.NotificationRepository;
import com.guvaren.notification.master.service.NotificationService;
import com.guvaren.notification.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationRes> getAll() {
        List<NotificationEntity> notifications = notificationRepository.findAll();
        return notifications.stream().map(this::toResponse).toList();
    }

    @Override
    public Optional<NotificationRes> sendTestNotification(NotificationReq request) {
        NotificationEntity notification = NotificationEntity.builder()
                .id(CommonUtil.getUUID())
                .type(request.getType())
                .message(request.getMessage())
                .recipient(request.getRecipient())
                .status("SENT")
                .build();

        notificationRepository.save(notification);
        return Optional.of(toResponse(notification));
    }

    private NotificationRes toResponse(NotificationEntity entity) {
        return NotificationRes.builder()
                .id(entity.getId())
                .type(entity.getType())
                .message(entity.getMessage())
                .recipient(entity.getRecipient())
                .status(entity.getStatus())
                .build();
    }
}

