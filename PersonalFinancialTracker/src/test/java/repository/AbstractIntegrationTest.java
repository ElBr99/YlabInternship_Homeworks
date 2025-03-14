package repository;

import org.junit.jupiter.api.AfterEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import utils.ConnectionManager;
import utils.PropertiesUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractIntegrationTest {

    private static final String PASSWORD_KEY = "db.password";
    private static final String USERNAME_KEY = "db.username";
    private static final String DATABASE_NAME = "db.dbName";
    private static final String PORT = "db.port";


    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine"));


    static {
        postgres.withExposedPorts(Integer.parseInt(PropertiesUtils.get(PORT)))
                .withDatabaseName(PropertiesUtils.get(DATABASE_NAME))
                .withUsername(PropertiesUtils.get(USERNAME_KEY))
                .withPassword(PropertiesUtils.get(PASSWORD_KEY))
                .waitingFor(Wait.forListeningPort())
                .start();
    }


    @AfterEach
    protected void clearDatabase() {
        try (Connection connection = ConnectionManager.get()) {
            Statement statement = connection.createStatement();
            String deleteSql = "truncate users, goals, budgets, transactions";
            statement.executeQuery(deleteSql);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

    }
}
