package repository;

import model.Role;
import model.User;
import utils.ConnectionManager;

import java.sql.SQLException;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {


    private static final String SAVE_SQL = " insert into entities.users (name, email, password, role,blocked) values (?, ?, ?,?,?) ";
    private static final String FIND_BY_EMAIL_SQL = "select * from entities.users where email = ? ";
    private static final String UPDATE_INFO = "update entities.users set name=?, email=?, password=?, blocked=? ";
    private static final String DELETE_USER = "delete * from entities.users";


    public void save(User user) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, String.valueOf(user.getRole()));
            preparedStatement.setBoolean(5, user.getBlocked());

            preparedStatement.executeQuery();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Optional<User> findByEmail(String email) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_SQL)) {
            preparedStatement.setString(2, email);
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
            preparedStatement.setBoolean(5, user.getBlocked());

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void delete(User user) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_INFO)) {
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
