package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.LoginService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntranceControllerTest {

    private final String testLogin = "test@example.com";
    private final String testPassword = "password";
    @Mock
    private LoginService loginService;
    @InjectMocks
    private EntranceController entranceController;

    @Test
    void execute_Scanner_ValidInput_CallsLoginServiceEnter() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine()).thenReturn(testLogin, testPassword);

        entranceController.execute(scanner);

        verify(loginService).enter(argThat(enterUserDto ->
                enterUserDto.getEmail().equals(testLogin) &&
                        enterUserDto.getPassword().equals(testPassword)
        ));
    }

    @Test
    void execute_PrintWriterBufferedReader_ValidInput_CallsLoginServiceEnter() throws IOException {
        PrintWriter out = mock(PrintWriter.class);
        BufferedReader in = mock(BufferedReader.class);
        when(in.readLine()).thenReturn(testLogin, testPassword);

        entranceController.execute(out, in);

        verify(loginService).enter(argThat(enterUserDto ->
                enterUserDto.getEmail().equals(testLogin) &&
                        enterUserDto.getPassword().equals(testPassword)
        ));
        verify(out, atLeastOnce()).println(anyString());
    }
}