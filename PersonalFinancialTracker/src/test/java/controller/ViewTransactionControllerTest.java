package controller;


import com.project.controller.ViewTransactionController;
import com.project.model.Transaction;
import com.project.model.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.service.TransactionService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ViewTransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private ViewTransactionController viewTransactionController;

    @Test
    void execute_Scanner_PrintsTransactions() {
        Scanner scanner = mock(Scanner.class);
        List<Transaction> mockTransactions = createMockTransactions();
        when(transactionService.viewTransactions()).thenReturn(mockTransactions);

        viewTransactionController.execute(scanner);

        verify(transactionService).viewTransactions();
    }

    @Test
    void execute_PrintWriterBufferedReader_PrintsTransactions() {
        PrintWriter out = mock(PrintWriter.class);
        BufferedReader in = mock(BufferedReader.class);

        List<Transaction> mockTransactions = createMockTransactions();
        when(transactionService.viewTransactions()).thenReturn(mockTransactions);

        viewTransactionController.execute(out, in);

        verify(transactionService).viewTransactions();
        verify(out, times(2)).println(anyString());
    }

    private List<Transaction> createMockTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createTransaction("Category1", TransactionType.INCOME, OffsetDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC), BigDecimal.valueOf(100), "Description1"));
        transactions.add(createTransaction("Category2", TransactionType.EXPENDITURE, OffsetDateTime.of(2024, 1, 2, 12, 0, 0, 0, ZoneOffset.UTC), BigDecimal.valueOf(50), "Description2"));
        return transactions;
    }

    private Transaction createTransaction(String category, TransactionType transactionType, OffsetDateTime dateTime, BigDecimal sum, String description) {
        Transaction transaction = new Transaction();
        transaction.setCategory(category);
        transaction.setTransactionType(transactionType);
        transaction.setDateTime(dateTime);
        transaction.setSum(sum);
        transaction.setDescription(description);
        return transaction;
    }
}