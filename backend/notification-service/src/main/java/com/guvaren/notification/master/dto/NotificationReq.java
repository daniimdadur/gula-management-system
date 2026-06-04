package com.guvaren.notification.master.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationReq {
    private String type;
    private String message;
    private String recipient;
}

