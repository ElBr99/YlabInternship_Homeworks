package service.impl;

import dto.ChangeTransInfoDto;
import dto.CreateTransactionDto;
import dto.FilterTransactionsDto;
import exception.TransactionNotFound;
import listener.CreateTransactionListener;
import listener.DeleteTransactionListener;
import lombok.RequiredArgsConstructor;
import model.Transaction;
import repository.TransactionRepository;
import service.TransactionService;
import utils.SecurityContext;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final List<CreateTransactionListener> transactionListeners;
    private final List<DeleteTransactionListener> deleteTransactionListeners;

    @Override
    public Transaction addTransaction(CreateTransactionDto createTransactionDto) {
        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                SecurityContext.getCurrentUserEmail(),
                createTransactionDto.getTransactionType(),
                createTransactionDto.getSum(),
                OffsetDateTime.now(),
                createTransactionDto.getDescription(),
                createTransactionDto.getCategory()
        );

        transactionRepository.save(transaction);
        transactionListeners.forEach(listener -> listener.onCreate(transaction));
        return transaction;
    }

    @Override
    public void changeTransactionInfo(UUID uuid, ChangeTransInfoDto changeTransInfoDto) {
        Transaction transaction = transactionRepository.findByUuid(uuid)
                .orElseThrow(() -> new TransactionNotFound("Такой транзакции не найдено"));

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
    public void deleteTransaction(UUID uuid) {
        Transaction transaction = transactionRepository.findByUuid(uuid)
                .orElseThrow(() -> new TransactionNotFound("Такой транзакции не найдено"));

        transactionRepository.delete(uuid);
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
