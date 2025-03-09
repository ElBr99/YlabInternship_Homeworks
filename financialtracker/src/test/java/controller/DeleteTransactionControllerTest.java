package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.TransactionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteTransactionControllerTest {

    private final UUID testUuid = UUID.randomUUID();
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private DeleteTransactionController deleteTransactionController;

    @Test
    void execute_Scanner_ValidInput_DeletesTransaction() {
        Scanner scanner = mock(Scanner.class);

        when(scanner.nextLine()).thenReturn(testUuid.toString());

        deleteTransactionController.execute(scanner);

        verify(transactionService).deleteTransaction(testUuid);
    }

    @Test
    void execute_PrintWriterBufferedReader_ValidInput_DeletesTransaction() throws IOException {
        PrintWriter out = mock(PrintWriter.class);
        BufferedReader in = mock(BufferedReader.class);

        when(in.readLine()).thenReturn(testUuid.toString());

        deleteTransactionController.execute(out, in);

        verify(transactionService).deleteTransaction(testUuid);
        verify(out).println(anyString());
    }
}