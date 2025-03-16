package com.project.repository;

import com.project.model.Goal;
import com.project.utils.ConnectionManager;

import java.sql.SQLException;
import java.util.Optional;

public class GoalRepositoryImpl implements GoalRepository {
    private static final String CREATE_SQL = "insert into entities.goals (user_email, goal, target_amount, current_amount ) values (?, ?, ?,?)";
    private static final String UPDATE_INFO = "update entities.goals set goal=?, target_amount=?, current_amount=?";
    private static final String GET_BY_USER_EMAIL = "select * from entities.goals where user_email = ? ";

    @Override
    public void createGoal(Goal goal) {

        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(CREATE_SQL)) {
            preparedStatement.setString(1, goal.getUserEmail());
            preparedStatement.setString(2, goal.getGoal());
            preparedStatement.setBigDecimal(3, goal.getTarget());
            preparedStatement.setBigDecimal(4, goal.getCurrent());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateGoal(Goal goal) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_INFO)) {
            preparedStatement.setString(1, goal.getGoal());
            preparedStatement.setBigDecimal(2, goal.getTarget());
            preparedStatement.setBigDecimal(3, goal.getCurrent());

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Optional<Goal> getGoalByUser(String userEmail) {

        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_BY_USER_EMAIL)) {
            preparedStatement.setString(1, userEmail);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet != null) {

                Goal goal = Goal.builder()
                        .id(resultSet.getInt("id"))
                        .userEmail(resultSet.getString("user_email"))
                        .goal(resultSet.getString("goal"))
                        .target(resultSet.getBigDecimal("target_amount"))
                        .current(resultSet.getBigDecimal("current_amount"))
                        .build();

                return Optional.of(goal);
            } else {
                return Optional.empty();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
