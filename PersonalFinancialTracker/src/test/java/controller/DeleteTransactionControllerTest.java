package controller;

import com.project.controller.DeleteTransactionController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.service.TransactionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteTransactionControllerTest {

    private final int testId = new Random().nextInt(10)+1;
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private DeleteTransactionController deleteTransactionController;

    @Test
    void execute_Scanner_ValidInput_DeletesTransaction() {
        Scanner scanner = mock(Scanner.class);

        when(scanner.nextLine()).thenReturn(String.valueOf(testId));

        deleteTransactionController.execute(scanner);

        verify(transactionService).deleteTransaction(testId);
    }

    @Test
    void execute_PrintWriterBufferedReader_ValidInput_DeletesTransaction() throws IOException {
        PrintWriter out = mock(PrintWriter.class);
        BufferedReader in = mock(BufferedReader.class);

        when(in.readLine()).thenReturn(String.valueOf(testId));

        deleteTransactionController.execute(out, in);

        verify(transactionService).deleteTransaction(testId);
        verify(out).println(anyString());
    }
}