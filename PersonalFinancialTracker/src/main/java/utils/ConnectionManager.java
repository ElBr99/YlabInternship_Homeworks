package utils;

import lombok.SneakyThrows;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class ConnectionManager {

    private static final String PASSWORD_KEY = "db.password";
    private static final String USERNAME_KEY = "db.username";
    private static final String URL_KEY = "db.url";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final Integer DEFAULT_POOL_SIZE = 10;
    private static BlockingQueue<Connection> pool;
    private static List<Connection> sourceConnections;

    static {
        loadDriver();
        initConnectionPool();
    }

    private ConnectionManager() {

    }

    private static void initConnectionPool() {
        var poolsize = PropertiesUtils.get(POOL_SIZE_KEY);
        var size = (poolsize == null) ? DEFAULT_POOL_SIZE : Integer.parseInt(poolsize);
        pool = new ArrayBlockingQueue<>(size);
        sourceConnections = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            var connection = open();
            var proxyConnection = (Connection) Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(), new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close") ?
                            pool.add((Connection) proxy)
                            : method.invoke(connection, args));
            pool.add(proxyConnection);
            sourceConnections.add(connection);
        }
    }

    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }


    @SneakyThrows
    private static Connection open() {
        return DriverManager.getConnection(
                PropertiesUtils.get(URL_KEY),
                PropertiesUtils.get(USERNAME_KEY),
                PropertiesUtils.get(PASSWORD_KEY)
        );
    }


    public static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void closePool() {
        try {
            for (Connection sourceConnection : sourceConnections) {
                sourceConnection.close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
