package com.project.controller;

import com.project.controller.ChangeTransactionController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.service.TransactionService;

import java.math.BigDecimal;
import java.util.Random;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChangeTransactionControllerTest {


    private final int testId = new Random().nextInt(10)+1;
    private final BigDecimal testSum = BigDecimal.valueOf(100);
    private final String testCategory = "Test Category";
    private final String testDescription = "Test Description";
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private ChangeTransactionController changeTransactionController;

    @Test
    void execute_MockedScanner_ValidInput_ChangesTransactionInfo() {
        Scanner scanner = mock(Scanner.class);

        when(scanner.nextLine()).thenReturn(String.valueOf(testId), testCategory, testDescription);
        when(scanner.nextBigDecimal()).thenReturn(testSum);

        changeTransactionController.execute(scanner);

        verify(transactionService).changeTransactionInfo(eq(testId), argThat(changeTransInfoDto ->
                changeTransInfoDto.getSum().equals(testSum) &&
                        changeTransInfoDto.getCategory().equals(testCategory) &&
                        changeTransInfoDto.getDescription().equals(testDescription)
        ));
    }

}