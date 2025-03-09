package repository;

import model.Transaction;
import model.TransactionType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionRepository {

    private static final Map<UUID, Transaction> transactionsMap = new ConcurrentHashMap<>();

    public void save(Transaction transaction) {
        transactionsMap.putIfAbsent(transaction.getUuid(), transaction);
    }


    public void update(Transaction transaction) {
        transactionsMap.put(transaction.getUuid(), transaction);
    }

    public Optional<Transaction> findByUuid(UUID uuid) {
        return Optional.ofNullable(transactionsMap.get(uuid));
    }

    public void delete(UUID uuid) {
        transactionsMap.remove(uuid);
    }


    public BigDecimal findIncome(String email) {

        return transactionsMap
                .values()
                .stream()
                .filter(transaction -> transaction.getTransactionType().equals(TransactionType.INCOME))
                .map(Transaction::getSum)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

    }

    public BigDecimal findExpenditure(String email) {
        return transactionsMap
                .values()
                .stream()
                .filter(transaction -> transaction.getTransactionType().equals(TransactionType.EXPENDITURE))
                .map(Transaction::getSum)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }


    public List<Transaction> getTransactionListByUserEmail(String userEmail) {
        return transactionsMap
                .values()
                .stream()
                .filter(transaction -> transaction.getUserEmail().equals(userEmail))
                .toList();
    }

    public List<Transaction> findAllExpenditures(String email) {
        return transactionsMap
                .values().stream()
                .filter(transaction -> transaction.getTransactionType().equals(TransactionType.EXPENDITURE))
                .toList();
    }

    public List<Transaction> findAllIncome(String email) {
        return transactionsMap
                .values()
                .stream()
                .filter(transaction -> transaction.getTransactionType().equals(TransactionType.INCOME))
                .toList();
    }


}
