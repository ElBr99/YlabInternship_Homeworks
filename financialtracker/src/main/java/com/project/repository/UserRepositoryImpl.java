package com.project.repository;

import com.project.model.Role;
import com.project.model.User;
import com.project.utils.ConnectionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final ConnectionManager connectionManager;

    private static final String SAVE_SQL = " insert into entities.users (name, email, password, role,blocked) values (?, ?, ?,?,?) ";
    private static final String FIND_BY_EMAIL_SQL = "select * from entities.users where email = ? ";
    private static final String UPDATE_INFO = "update entities.users set name=?, password=?, blocked=? where email=?";
    private static final String DELETE_USER = "delete from entities.users where email = ?";


    public void save(User user) {
        Connection connection = null;
        try {
            connection = connectionManager.get();
            try (var preparedStatement = connection.prepareStatement(SAVE_SQL)) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setString(4, String.valueOf(user.getRole()));
                preparedStatement.setBoolean(5, user.getBlocked());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            connectionManager.release(connection);
        }
    }

    public Optional<User> findByEmail(String email) {
        Connection connection = null;
        try {
            connection = connectionManager.get();
            try (var preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_SQL)) {
                preparedStatement.setString(1, email);
                var resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    User user = new User(resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            Role.valueOf(resultSet.getString("role")),
                            resultSet.getBoolean("blocked"));
                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            connectionManager.release(connection);
        }
    }

    public void update(User user) {
        Connection connection = null;
        try {
            connection = connectionManager.get();
            try (var preparedStatement = connection.prepareStatement(UPDATE_INFO)) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setBoolean(3, user.getBlocked());
                preparedStatement.setString(4, user.getEmail());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            connectionManager.release(connection);
        }
    }

    public void delete(User user) {
        Connection connection = null;
        try {
            connection = connectionManager.get();
            try (var preparedStatement = connection.prepareStatement(DELETE_USER)) {
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            connectionManager.release(connection);
        }
    }
}
