package service;

import dto.ChangeTransInfoDto;
import dto.CreateTransactionDto;
import dto.FilterTransactionsDto;
import model.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    Transaction addTransaction(CreateTransactionDto createTransactionDto);

    void changeTransactionInfo(int id, ChangeTransInfoDto changeTransInfoDto);

    void deleteTransaction(int id);

    List<Transaction> viewTransactions();

    List<Transaction> viewTransactionsByUserEmail(String email);

    List<Transaction> filterTransactions(FilterTransactionsDto filterTransactionsDto);

}
