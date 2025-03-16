package com.project.service.impl;

import com.project.service.impl.EmailNotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailNotificationServiceTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private EmailNotificationService notificationService;

    @BeforeEach
    public void setUp() {
        notificationService = new EmailNotificationService();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void notify_SendsCorrectMessage() {
        String userId = "test@example.com";
        String message = "Test message";
        String expectedOutput = String.format("Отправка сообщения %s на %s", message, userId);

        notificationService.notify(userId, message);

        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }
}