package com.project.service.impl;


import com.project.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService implements NotificationService {
    @Override
    public void notify(String userId, String message) {
        System.out.printf("Отправка сообщения %s на %s", message, userId);
    }
}
