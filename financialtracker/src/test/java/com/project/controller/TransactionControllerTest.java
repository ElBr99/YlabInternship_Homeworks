package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dtos.ChangeTransInfoDto;
import com.project.dtos.FilterTransactionsDto;
import com.project.model.Transaction;
import com.project.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void getCurrentUserTransactions_ShouldReturnListOfTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());

        when(transactionService.viewTransactions()).thenReturn(transactions);

        mockMvc.perform(get("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transactions)));
    }

    @Test
    void getCurrentUserTransactionsByEmail_ShouldReturnTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());

        String email = "test@example.com";
        when(transactionService.viewTransactionsByUserEmail(email)).thenReturn(transactions);

        mockMvc.perform(get("/api/v1/transactions/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transactions)));
    }

    @Test
    void getCurrentUserTransactionsWithFilter_ShouldReturnFilteredTransactions() throws Exception {

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        when(transactionService.filterTransactions(any(FilterTransactionsDto.class))).thenReturn(transactions);

        mockMvc.perform(get("/api/v1/transactions/filter")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-12-31")
                        .param("category", "Food")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transactions)));

        verify(transactionService).filterTransactions(any(FilterTransactionsDto.class));
    }

    @Test
    void updateTransaction_ShouldChangeTransactionInfo() throws Exception {
        Integer id = 1;
        ChangeTransInfoDto changeTransInfoDto = new ChangeTransInfoDto();


        mockMvc.perform(put("/api/v1/transactions/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeTransInfoDto)))
                .andExpect(status().isOk());

        verify(transactionService).changeTransactionInfo(eq(id), any(ChangeTransInfoDto.class));
    }

    @Test
    void deleteTransaction_ShouldCallDeleteService() throws Exception {
        Integer id = 1;
        mockMvc.perform(delete("/api/v1/transactions/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(transactionService).deleteTransaction(id);
    }
}