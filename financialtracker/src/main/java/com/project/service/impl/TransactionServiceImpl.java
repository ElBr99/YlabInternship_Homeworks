package com.project.service.impl;

import com.project.dtos.ChangeTransInfoDto;
import com.project.dtos.CreateTransactionDto;
import com.project.dtos.FilterTransactionsDto;
import com.project.exceptions.TransactionNotFoundException;
import com.project.listener.CreateTransactionListener;
import com.project.listener.DeleteTransactionListener;
import lombok.RequiredArgsConstructor;
import com.project.model.Transaction;
import com.project.repository.TransactionRepository;
import com.project.service.TransactionService;
import com.project.utils.SecurityContext;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final List<CreateTransactionListener> transactionListeners;
    private final List<DeleteTransactionListener> deleteTransactionListeners;

    @Override
    public Transaction addTransaction(CreateTransactionDto createTransactionDto) {

        Transaction transaction = Transaction.builder()
                .userEmail(SecurityContext.getCurrentUserEmail())
                .transactionType(createTransactionDto.getTransactionType())
                .sum(createTransactionDto.getSum())
                .dateTime(OffsetDateTime.now())
                .description(createTransactionDto.getDescription())
                .category(createTransactionDto.getCategory())
                .build();

        transactionRepository.save(transaction);
        transactionListeners.forEach(listener -> listener.onCreate(transaction));
        return transaction;
    }

    @Override
    public void changeTransactionInfo(int id, ChangeTransInfoDto changeTransInfoDto) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Такой транзакции не найдено"));

        if (changeTransInfoDto.getSum() != null) {
            transaction.setSum(changeTransInfoDto.getSum());
        }
        if (changeTransInfoDto.getCategory() != null) {
            transaction.setCategory(changeTransInfoDto.getCategory());
        }
        if (changeTransInfoDto.getDescription() != null) {
            transaction.setDescription(changeTransInfoDto.getDescription());
        }


        transactionRepository.update(transaction);
    }


    @Override
    public void deleteTransaction(int id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Такой транзакции не найдено"));

        transactionRepository.delete(id);
        deleteTransactionListeners.forEach(listener -> listener.onDelete(transaction));
    }

    @Override
    public List<Transaction> viewTransactions() {
        return transactionRepository.getTransactionListByUserEmail(SecurityContext.getCurrentUserEmail());
    }

    @Override
    public List<Transaction> viewTransactionsByUserEmail(String email) {
        return transactionRepository.getTransactionListByUserEmail(email);
    }

    @Override
    public List<Transaction> filterTransactions(FilterTransactionsDto filter) {
        return viewTransactions()
                .stream()
                .filter(transaction ->
                        filter.getTransactionType() == null ||
                                filter.getTransactionType().equals(transaction.getTransactionType())
                )
                .filter(transaction ->
                        filter.getCategory() == null ||
                                filter.getCategory().equalsIgnoreCase(transaction.getCategory())
                )
                .filter(transaction ->
                        filter.getFrom() == null ||
                                filter.getFrom().isBefore(transaction.getDateTime().toLocalDate()) ||
                                filter.getFrom().isEqual(transaction.getDateTime().toLocalDate())
                )
                .filter(transaction ->
                        filter.getTo() == null ||
                                filter.getTo().isAfter(transaction.getDateTime().toLocalDate()) ||
                                filter.getTo().isEqual(transaction.getDateTime().toLocalDate())
                )
                .toList();
    }


}
