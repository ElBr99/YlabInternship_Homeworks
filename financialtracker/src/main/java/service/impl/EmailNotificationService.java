package service.impl;


import service.NotificationService;

public class EmailNotificationService implements NotificationService {
    @Override
    public void notify(String userId, String message) {
        System.out.printf("Отправка сообщения %s на %s", message, userId);
    }
}
