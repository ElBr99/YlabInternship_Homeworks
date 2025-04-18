package com.project.utils;

import org.junit.jupiter.api.AfterEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractIntegrationTest {

    private static final String PASSWORD_KEY = "db.password";
    private static final String USERNAME_KEY = "db.username";
    private static final String URL_KEY = "db.url";


    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine"));


    static {
        postgres
                .withInitScript("init.sql")
                .start();

        PropertiesUtils.set(PASSWORD_KEY, postgres.getPassword());
        PropertiesUtils.set(USERNAME_KEY, postgres.getUsername());
        PropertiesUtils.set(URL_KEY, postgres.getJdbcUrl());

        LiquibaseUtils.launchMigrations();
    }


    @AfterEach
    void clearDatabase() {
        Connection connection = null;
        try {
            connection = ConnectionManager.get();
            try (Statement statement = connection.createStatement()) {
                String deleteSql = "truncate entities.users, entities.goals, entities.budgets, entities.transactions";
                statement.executeUpdate(deleteSql);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            ConnectionManager.release(connection);
        }

    }
}
