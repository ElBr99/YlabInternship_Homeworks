package com.project.controller;

import com.project.dtos.DatePeriodDto;
import com.project.model.FinancialReport;
import com.project.service.FinancialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FinancialReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FinancialService financialService;

    @InjectMocks
    private FinancialReportController financialReportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(financialReportController).build();
    }

    @Test
    void findCurrentFinancialStatement_ShouldReturnStatement() throws Exception {
        BigDecimal expectedStatement = BigDecimal.valueOf(1000.00);

        when(financialService.findCurrentFinancialStatement()).thenReturn(expectedStatement);

        mockMvc.perform(get("/api/v1/financial-reports/statement")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedStatement.toString()));
    }

    @Test
    void generateReport_ShouldReturnFinancialReport() throws Exception {
        FinancialReport expectedReport = new FinancialReport();

        when(financialService.generateReport()).thenReturn(expectedReport);

        mockMvc.perform(get("/api/v1/financial-reports/report")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void findExpenseForPeriod_ShouldReturnExpense() throws Exception {
        DatePeriodDto datePeriodDto = new DatePeriodDto();
        BigDecimal expectedExpense = BigDecimal.valueOf(400.00);

        when(financialService.findExpenseForPeriod(any(DatePeriodDto.class))).thenReturn(expectedExpense);

        mockMvc.perform(get("/api/v1/financial-reports/expense")
                        .param("startDate", "2022-01-01")
                        .param("endDate", "2022-12-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedExpense.toString()));
    }

    @Test
    void findExpenseByCategories_ShouldReturnExpenseMap() throws Exception {
        Map<String, BigDecimal> expectedExpenses = new HashMap<>();
        expectedExpenses.put("Food", BigDecimal.valueOf(200.00));
        expectedExpenses.put("Transport", BigDecimal.valueOf(150.00));

        when(financialService.showExpenseByCategories()).thenReturn(expectedExpenses);

        mockMvc.perform(get("/api/v1/financial-reports/expense-by-category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"Food\":200.00,\"Transport\":150.00}"));
    }
}