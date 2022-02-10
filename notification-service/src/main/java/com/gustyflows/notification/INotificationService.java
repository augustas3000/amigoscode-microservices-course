package com.gustyflows.notification;

import com.gustyflows.clients.notification.NotificationRequest;

public interface INotificationService {
    void send(NotificationRequest notificationRequest);
}
