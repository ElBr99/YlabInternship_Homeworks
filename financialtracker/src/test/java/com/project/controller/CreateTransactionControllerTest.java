package com.project.controller;

import com.project.controller.CreateTransactionController;
import com.project.model.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.service.TransactionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateTransactionControllerTest {

    private final String testType = "INCOME";
    private final BigDecimal testSum = BigDecimal.valueOf(100);
    private final String testDescription = "Test Description";
    private final String testCategory = "Test Category";
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private CreateTransactionController createTransactionController;

    @Test
    void execute_Scanner_InvalidType_ThrowsIllegalArgumentException() {
        String input = String.format("%s\n%s\n%s\n%s", "INVALID", testSum, testDescription, testCategory);
        Scanner scanner = new Scanner(input);
        scanner.useDelimiter("\n");

        assertThrows(IllegalArgumentException.class, () -> createTransactionController.execute(scanner));
        verify(transactionService, never()).addTransaction(any());
    }

    @Test
    void execute_PrintWriterBufferedReader_ValidInput_AddsTransaction() throws IOException {
        PrintWriter out = mock(PrintWriter.class);
        BufferedReader in = mock(BufferedReader.class);

        when(in.readLine()).thenReturn(testType, testSum.toString(), testDescription, testCategory);

        createTransactionController.execute(out, in);

        verify(transactionService).addTransaction(argThat(createTransactionDto ->
                createTransactionDto.getTransactionType().equals(TransactionType.INCOME) &&
                        createTransactionDto.getSum().equals(testSum) &&
                        createTransactionDto.getDescription().equals(testDescription) &&
                        createTransactionDto.getCategory().equals(testCategory)
        ));
        verify(out, atLeastOnce()).println(anyString());
    }

    @Test
    void execute_PrintWriterBufferedReader_InvalidType_ThrowsIllegalArgumentException() throws IOException {
        PrintWriter out = mock(PrintWriter.class);
        BufferedReader in = mock(BufferedReader.class);

        when(in.readLine()).thenReturn("INVALID", testSum.toString(), testDescription, testCategory);

        assertThrows(IllegalArgumentException.class, () -> createTransactionController.execute(out, in));
        verify(transactionService, never()).addTransaction(any());
        verify(out, atLeastOnce()).println(anyString());
    }
}