package com.project.controller;

import com.project.controller.DeleteUserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteUserControllerTest {

    private final String testEmail = "test@example.com";
    @Mock
    private UserService userService;
    @InjectMocks
    private DeleteUserController deleteUserController;

    @Test
    void execute_Scanner_DeletesUserAccount() {
        Scanner scanner = mock(Scanner.class);

        when(scanner.nextLine()).thenReturn(testEmail);

        deleteUserController.execute(scanner);

        verify(userService).deleteAccount(testEmail);
    }

    @Test
    void execute_PrintWriterBufferedReader_DeletesUserAccount() throws IOException {
        PrintWriter out = mock(PrintWriter.class);
        BufferedReader in = mock(BufferedReader.class);

        when(in.readLine()).thenReturn(testEmail);

        deleteUserController.execute(out, in);

        verify(userService).deleteAccount(testEmail);
        verify(out).println(anyString());
    }
}