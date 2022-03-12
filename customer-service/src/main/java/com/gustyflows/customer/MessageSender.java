package com.gustyflows.customer;

import com.gustyflows.clients.notification.NotificationRequest;

public interface MessageSender {
    void send(NotificationRequest notificationRequest);
}
