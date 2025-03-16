package com.project.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
public final class ConnectionManager {
    private static final String PASSWORD_KEY = "db.password";
    private static final String USERNAME_KEY = "db.username";
    private static final String URL_KEY = "db.url";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static BlockingQueue<Connection> pool;

    static {
        loadDriver();
        initConnectionPool();
        LiquibaseUtils.launchMigrations();
    }

    private static void initConnectionPool() {
        int size = Integer.parseInt(PropertiesUtils.get(POOL_SIZE_KEY));
        pool = new ArrayBlockingQueue<>(size);
        for (int i = 0; i < size; i++) {
            pool.add(open());
        }
    }

    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Unable to get connection from pool", exception);
        }
    }

    private static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtils.get(URL_KEY),
                    PropertiesUtils.get(USERNAME_KEY),
                    PropertiesUtils.get(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            log.error("Error opening a new connection", e);
            throw new RuntimeException("Error opening a new connection", e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("PostgreSQL driver not found", exception);
        }
    }

    public static void release(Connection connection) {
        if (connection != null) {
            try {
                // Если соединение не закрыто, добавляем его обратно в пул
                // Здесь мы просто проверяем, если соединение открыто
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

    public static void closePool() {
        try {
            for (Connection connection : pool) {
                connection.close(); // Закрываем каждое соединение
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error closing connection pool", e);
        }
    }
}
