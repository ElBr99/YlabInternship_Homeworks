package com.project.repository;

import com.project.model.Transaction;
import com.project.model.TransactionType;
import com.project.utils.ConnectionManager;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionRepository {

    private static final String SAVE_SQL = "insert into entities.transactions (user_email,transaction_type, amount, date_time, description, category) values (?, ?, ?,?,?,?) ";
    private static final String UPDATE_SQL = "update entities.transactions set amount=?, description=?, category=?";
    private static final String FIND_BY_ID = "select * from entities.transactions where id = ?";
    private static final String DELETE_TRANSACTION = "delete * from entities.transactions";
    private static final String FIND_INCOME = "select count(amount) from entities.transactions where user_email = ? and transaction_type ='INCOME' ";
    private static final String FIND_EXPENDITURE = "select count(amount) from entities.transactions where user_email = ? and transaction_type ='EXPENDITURE' ";
    private static final String GET_ALL_TRANSACTIONS = "select * from entities.transactions where user_email = ?";
    private static final String GET_ALL_EXPENDITURES_TRANSACTIONS = "select * from entities.transactions where user_email = ? and transaction_type ='EXPENDITURE' ";
    private static final String GET_ALL_INCOME_TRANSACTIONS = "select * from entities.transactions where user_email = ? and transaction_type ='INCOME' ";


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

    public void delete(int id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_TRANSACTION)) {
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

    }


    public BigDecimal findIncome(String email) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_INCOME)) {
            preparedStatement.setString(1, email);
            return BigDecimal.valueOf(preparedStatement.executeUpdate());
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

    }

    public BigDecimal findExpenditure(String email) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_EXPENDITURE)) {
            preparedStatement.setString(1, email);
            return BigDecimal.valueOf(preparedStatement.executeUpdate());
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }


    public List<Transaction> getTransactionListByUserEmail(String userEmail) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_ALL_TRANSACTIONS)) {
            preparedStatement.setString(1, userEmail);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Transaction> transactionList = new ArrayList<>();
            while (resultSet.next()) {
                Transaction transaction = Transaction.builder()
                        .id(resultSet.getInt("id"))
                        .userEmail(resultSet.getString("user_email"))
                        .transactionType(TransactionType.valueOf(resultSet.getString("transaction_type")))
                        .sum(resultSet.getBigDecimal("amount"))
                        .dateTime(OffsetDateTime.from((resultSet.getTime("date_time")).toLocalTime()))
                        .description(resultSet.getString("description"))
                        .category(resultSet.getString("category"))
                        .build();
                transactionList.add(transaction);
            }
            return transactionList;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public List<Transaction> findAllExpenditures(String email) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_ALL_EXPENDITURES_TRANSACTIONS)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Transaction> transactionExpList = new ArrayList<>();
            while (resultSet.next()) {
                Transaction transaction = Transaction.builder()
                        .id(resultSet.getInt("id"))
                        .userEmail(resultSet.getString("user_email"))
                        .transactionType(TransactionType.valueOf(resultSet.getString("transaction_type")))
                        .sum(resultSet.getBigDecimal("amount"))
                        .dateTime(OffsetDateTime.from((resultSet.getTime("date_time")).toLocalTime()))
                        .description(resultSet.getString("description"))
                        .category(resultSet.getString("category"))
                        .build();
                transactionExpList.add(transaction);
            }
            return transactionExpList;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public List<Transaction> findAllIncome(String email) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_ALL_INCOME_TRANSACTIONS)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Transaction> transactionIncomeList = new ArrayList<>();
            while (resultSet.next()) {
                Transaction transaction = Transaction.builder()
                        .id(resultSet.getInt("id"))
                        .userEmail(resultSet.getString("user_email"))
                        .transactionType(TransactionType.valueOf(resultSet.getString("transaction_type")))
                        .sum(resultSet.getBigDecimal("amount"))
                        .dateTime(OffsetDateTime.from((resultSet.getTime("date_time")).toLocalTime()))
                        .description(resultSet.getString("description"))
                        .category(resultSet.getString("category"))
                        .build();
                transactionIncomeList.add(transaction);
            }
            return transactionIncomeList;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }


}
