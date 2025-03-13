package repository;

import model.Transaction;
import model.TransactionType;
import utils.ConnectionManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionRepository {

    private static final Map<UUID, Transaction> transactionsMap = new ConcurrentHashMap<>();

    private static final String SAVE_SQL = "insert into entities.transactions (user_email,transaction_type, amount, date_time, description, category) values (?, ?, ?,?,?,?) ";
    private static final String UPDATE_SQL = "update entities.transactions set amount=?, description=?, category=?";
    private static final String FIND_BY_ID = "select * from entities.transactions where id = ?";

    public void save(Transaction transaction) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, transaction.getUserEmail());
            preparedStatement.setString(2, String.valueOf(transaction.getTransactionType()));
            preparedStatement.setBigDecimal(3, transaction.getSum());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(transaction.getDateTime().toLocalDateTime()));
            preparedStatement.setString(5, transaction.getDescription());
            preparedStatement.setString(6, transaction.getCategory());

            preparedStatement.executeQuery();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void update(Transaction transaction) {

        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setBigDecimal(1, transaction.getSum());
            preparedStatement.setString(2, transaction.getDescription());
            preparedStatement.setString(3, transaction.getCategory());

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Optional<Transaction> findById(int id) {

        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet != null) {

                Transaction transaction = Transaction.builder()
                        .userEmail(resultSet.getString(2))
                        .transactionType(TransactionType.valueOf(resultSet.getString(3)))
                        .sum(resultSet.getBigDecimal(4))
                        .dateTime(OffsetDateTime.from(resultSet.getTimestamp(5).toLocalDateTime()))
                        .description(resultSet.getString(6))
                        .category(resultSet.getString(7))
                        .build();
                return Optional.of(transaction);
            } else {
                return Optional.empty();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
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
