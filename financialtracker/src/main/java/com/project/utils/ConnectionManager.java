package com.project.utils;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Component
public final class ConnectionManager {
    @Value("${db.password}")
    private String password;
    @Value("${db.username}")
    private String username;
    @Value("${db.url}")
    private String url;
    @Value("${db.pool.size}")
    private Integer poolSize;

    private BlockingQueue<Connection> pool = null;

    @PostConstruct
    private void initConnectionPool() {
        loadDriver();

        pool = new ArrayBlockingQueue<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            pool.add(open());
        }
    }

    public Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Unable to get connection from pool", exception);
        }
    }

    @SneakyThrows
    private Connection open() {
        return DriverManager.getConnection(
                url,
                username,
                password
        );
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("PostgreSQL driver not found", exception);
        }
    }

    public void release(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    pool.offer(connection);
                } else {
                    log.warn("Attempted to release a closed connection: " + connection);
                }
            } catch (SQLException e) {
                log.error("Failed to release connection back to pool", e);
            }
        }
    }
}
