package com.gustyflows.clients.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("notification-service")
public interface NotificationServiceClient {

    @PostMapping("api/v1/notification")
    void sendNotification(NotificationRequest notificationRequest);
}
