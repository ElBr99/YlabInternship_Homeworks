package com.project.controller;

import com.project.controller.BlockUserController;
import com.project.exceptions.UserNotFound;
import com.project.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlockUserControllerTest {

    private final String testEmail = "test@example.com";
    @Mock
    private UserService userService;
    @InjectMocks
    private BlockUserController blockUserController;

    @Test
    void execute_Scanner_UserFound_BlocksUser() {
        Scanner scanner = new Scanner(testEmail);
        User user = createUser("Test User", testEmail, "password", false);

        when(userService.findByEmail(testEmail)).thenReturn(Optional.of(user));

        blockUserController.execute(scanner);

        verify(userService).changeInfo(argThat(changeInfoDto ->
                changeInfoDto.getEmail().equals(testEmail) &&
                        changeInfoDto.getBlock()
        ));
    }

    @Test
    void execute_Scanner_UserNotFound_ThrowsUserNotFound() {
        Scanner scanner = new Scanner(testEmail);

        when(userService.findByEmail(testEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> blockUserController.execute(scanner));
        verify(userService, never()).changeInfo(any());
    }

    @Test
    void execute_PrintWriterBufferedReader_UserFound_BlocksUser() throws IOException {
        PrintWriter out = mock(PrintWriter.class);
        BufferedReader in = mock(BufferedReader.class);
        User user = createUser("Test User", testEmail, "password", false);

        when(in.readLine()).thenReturn(testEmail);
        when(userService.findByEmail(testEmail)).thenReturn(Optional.of(user));

        blockUserController.execute(out, in);

        verify(userService).changeInfo(argThat(changeInfoDto ->
                changeInfoDto.getEmail().equals(testEmail) &&
                        changeInfoDto.getBlock()
        ));
        verify(out).println(anyString());
    }

    @Test
    void execute_PrintWriterBufferedReader_UserNotFound_ThrowsUserNotFound() throws IOException {
        PrintWriter out = mock(PrintWriter.class);
        BufferedReader in = mock(BufferedReader.class);

        when(in.readLine()).thenReturn(testEmail);
        when(userService.findByEmail(testEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> blockUserController.execute(out, in));
        verify(userService, never()).changeInfo(any());
        verify(out).println(anyString());
    }

    private User createUser(String name, String email, String password, boolean blocked) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setBlocked(blocked);
        return user;
    }
}