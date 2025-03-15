package com.project.utils;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.sql.Connection;

public final class LiquibaseUtils {

    private static final String LIQUIBASE_CHANGELOG = "db.liquibase.changelog";
    private static final String LIQUIBASE_SCHEMA = "db.liquibase.defaultSchema";
    private static final String LIQUIBASE_MAIN_SCHEMA = "db.liquibase.mainSchema";

    @SneakyThrows
    public static Liquibase initLiquibase(Connection connection) {
        var database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

        database.setLiquibaseSchemaName(PropertiesUtils.get(LIQUIBASE_SCHEMA));
        database.setDefaultSchemaName(PropertiesUtils.get(LIQUIBASE_MAIN_SCHEMA));

        return new Liquibase(
                PropertiesUtils.get(LIQUIBASE_CHANGELOG),
                new ClassLoaderResourceAccessor(),
                database
        );
    }

    @SneakyThrows
    public static void launchMigrations() {
        @Cleanup Connection connection = ConnectionManager.get();
        var liquibase = initLiquibase(connection);
        liquibase.update("");
    }
}
