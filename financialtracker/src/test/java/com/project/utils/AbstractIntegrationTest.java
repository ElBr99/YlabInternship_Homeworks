package com.project.utils;

import com.project.ApplicationRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


@ActiveProfiles("test")
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationRunner.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestPropertySource(locations = "classpath:application-test.yaml")
public abstract class AbstractIntegrationTest {

    @Autowired
    private ConnectionManager connectionManager;

    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine"));


    static {
        postgres
                .withInitScript("init.sql")
                .start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("db.url", postgres::getJdbcUrl);
        registry.add("db.username", postgres::getUsername);
        registry.add("db.password", postgres::getPassword);
    }

    @AfterEach
    void clearDatabase() {
        Connection connection = null;
        try {
            connection = connectionManager.get();
            try (Statement statement = connection.createStatement()) {
                String deleteSql = "truncate entities.users, entities.goals, entities.budgets, entities.transactions";
                statement.executeUpdate(deleteSql);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            connectionManager.release(connection);
        }

    }
}
