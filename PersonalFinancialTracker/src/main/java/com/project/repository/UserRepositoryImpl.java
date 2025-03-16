package com.project.repository;

import com.project.model.Role;
import com.project.model.User;
import com.project.utils.ConnectionManager;

import java.sql.SQLException;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {


    private static final String SAVE_SQL = " insert into entities.users (name, email, password, role,blocked) values (?, ?, ?,?,?) ";
    private static final String FIND_BY_EMAIL_SQL = "select * from entities.users where email = ? ";
    private static final String UPDATE_INFO = "update entities.users set name=?, email=?, password=?, blocked=? where email =? ";
    private static final String DELETE_USER = "delete from entities.users where email = ?";


    public void save(User user) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, String.valueOf(user.getRole()));
            preparedStatement.setBoolean(5, user.getBlocked());

            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Optional<User> findByEmail(String email) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_SQL)) {
            preparedStatement.setString(1, email);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet != null) {

                User user = new User(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        Role.valueOf(resultSet.getString(4)),
                        resultSet.getBoolean(5));
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

    }


    public void update(User user) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_INFO)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setBoolean(4, user.getBlocked());
            preparedStatement.setString(5, user.getEmail());

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void delete(User user) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_USER)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.execute();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
