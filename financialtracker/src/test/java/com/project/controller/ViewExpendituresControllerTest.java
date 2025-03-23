package com.project.controller;

import com.project.controller.ViewExpendituresController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.service.FinancialService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ViewExpendituresControllerTest {

    @Mock
    private FinancialService financialService;

    @InjectMocks
    private ViewExpendituresController viewExpendituresController;

    @Test
    void execute_Scanner_PrintsExpensesByCategories() {
        Scanner scanner = mock(Scanner.class);

        Map<String, BigDecimal> mockExpenses = new HashMap<>();
        mockExpenses.put("Food", BigDecimal.valueOf(50.0));
        mockExpenses.put("Travel", BigDecimal.valueOf(100.0));

        when(financialService.showExpenseByCategories()).thenReturn(mockExpenses);

        viewExpendituresController.execute(scanner);

        verify(financialService).showExpenseByCategories();
    }

    @Test
    void execute_PrintWriterBufferedReader_PrintsExpensesByCategories() throws IOException {
        PrintWriter out = mock(PrintWriter.class);
        BufferedReader in = mock(BufferedReader.class);

        Map<String, BigDecimal> mockExpenses = new HashMap<>();
        mockExpenses.put("Food", BigDecimal.valueOf(50.0));
        mockExpenses.put("Travel", BigDecimal.valueOf(100.0));

        when(financialService.showExpenseByCategories()).thenReturn(mockExpenses);

        viewExpendituresController.execute(out, in);

        verify(financialService).showExpenseByCategories();
        verify(out).println("Food50.0");
        verify(out).println("Travel100.0");
    }
}