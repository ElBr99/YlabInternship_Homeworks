package com.project.service;

import com.project.dtos.ChangeTransInfoDto;
import com.project.dtos.CreateTransactionDto;
import com.project.dtos.FilterTransactionsDto;
import com.project.model.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction addTransaction(CreateTransactionDto createTransactionDto);

    void changeTransactionInfo(int id, ChangeTransInfoDto changeTransInfoDto);

    void deleteTransaction(int id);

    List<Transaction> viewTransactions();

    List<Transaction> viewTransactionsByUserEmail(String email);

    List<Transaction> filterTransactions(FilterTransactionsDto filterTransactionsDto);

}
