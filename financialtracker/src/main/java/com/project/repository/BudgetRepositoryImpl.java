package com.project.repository;

import com.project.model.Budget;
import com.project.utils.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class BudgetRepositoryImpl implements BudgetRepository {

    private static final String CREATE_SQL = "insert into entities.budgets (user_email,amount ) values (?, ?)";
    private static final String GET_BY_USER_EMAIL = "select amount from entities.budgets where user_email = ? ";


    @Override
    public Optional<BigDecimal> findByUser(String userEmail) {

        Connection connection = null;

        try {
            connection = ConnectionManager.get();
            try (var preparedStatement = connection.prepareStatement(GET_BY_USER_EMAIL)) {
                preparedStatement.setString(1, userEmail);
                var resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return Optional.of(resultSet.getBigDecimal("amount"));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            ConnectionManager.release(connection);
        }
    }

    @Override
    public void setBudgetForUser(Budget budget) {

        Connection connection = null;

        try {
            connection = ConnectionManager.get();
            try (var preparedStatement = connection.prepareStatement(CREATE_SQL)) {

                preparedStatement.setString(1, budget.getUserEmail());
                preparedStatement.setBigDecimal(2, budget.getAmount());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            ConnectionManager.release(connection);
        }
    }
}
