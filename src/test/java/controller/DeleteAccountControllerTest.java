package controller;

import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import service.UserService;
import utils.SecurityContext;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Scanner;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteAccountControllerTest {

    private final String testEmail = "test@example.com";
    @Mock
    private UserService userService;
    @InjectMocks
    private DeleteAccountController deleteAccountController;

    @Test
    void execute_Scanner_DeletesAccount() {
        User user = createUser(testEmail);

        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserInfo).thenReturn(user);

            Scanner scanner = new Scanner("");

            deleteAccountController.execute(scanner);

            verify(userService).deleteAccount(testEmail);
        }
    }

    @Test
    void execute_PrintWriterBufferedReader_DeletesAccount() {
        User user = createUser(testEmail);

        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserInfo).thenReturn(user);

            PrintWriter out = mock(PrintWriter.class);
            BufferedReader in = mock(BufferedReader.class);

            deleteAccountController.execute(out, in);

            verify(userService).deleteAccount(testEmail);
        }
    }

    private User createUser(String email) {
        User user = new User();
        user.setEmail(email);
        return user;
    }
}