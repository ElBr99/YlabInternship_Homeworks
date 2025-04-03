package com.project.controller;

import com.project.dtos.ChangeTransInfoDto;
import com.project.dtos.CreateTransactionDto;
import com.project.dtos.FilterTransactionsDto;
import com.project.model.Transaction;
import com.project.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('USER')")
@RequiredArgsConstructor
@RequestMapping("api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<Transaction> getCurrentUserTransactions() {
        return transactionService.viewTransactions();
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Transaction> getCurrentUserTransactions(@PathVariable("email") String email) {
        return transactionService.viewTransactionsByUserEmail(email);
    }

    @GetMapping("/filter")
    public List<Transaction> getCurrentUserTransactions(FilterTransactionsDto filterTransactionsDto) {
        return transactionService.filterTransactions(filterTransactionsDto);
    }

    @PostMapping
    public void createTransaction(@RequestBody CreateTransactionDto createTransactionDto) {
        transactionService.addTransaction(createTransactionDto);
    }

    @PutMapping("/{id}")
    public void updateTransaction(@PathVariable("id") Integer id, ChangeTransInfoDto changeTransInfoDto) {
        transactionService.changeTransactionInfo(id, changeTransInfoDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable("id") Integer id) {
        transactionService.deleteTransaction(id);
    }
}

