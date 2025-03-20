package com.project.service.impl;


import com.project.service.NotificationService;

public class EmailNotificationService implements NotificationService {
    @Override
    public void notify(String userId, String message) {
        System.out.printf("Отправка сообщения %s на %s", message, userId);
    }
}
