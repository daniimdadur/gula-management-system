package com.guvaren.notification.master.controller;

import com.guvaren.notification.base.BaseController;
import com.guvaren.notification.base.Response;
import com.guvaren.notification.master.dto.NotificationReq;
import com.guvaren.notification.master.dto.NotificationRes;
import com.guvaren.notification.master.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController extends BaseController<NotificationRes> {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Response> list() {
        return super.getResponse(notificationService.getAll());
    }

    @PostMapping("/test")
    public ResponseEntity<Response> sendTestNotification(@RequestBody NotificationReq request) {
        return super.getResponse(notificationService.sendTestNotification(request));
    }
}

