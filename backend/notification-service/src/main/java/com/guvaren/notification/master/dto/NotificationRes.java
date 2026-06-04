package com.guvaren.notification.master.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRes {
    private String id;
    private String type;
    private String message;
    private String recipient;
    private String status;
}

