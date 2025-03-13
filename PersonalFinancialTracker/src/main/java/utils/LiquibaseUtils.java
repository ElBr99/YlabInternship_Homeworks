package utils;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;

public final class LiquibaseUtils {

    private static final String LIQUIBASE_CHANGELOG = "db.liquibase.changelog";


    public static Liquibase initLiquibase(Connection connection) {
        try {
            return new Liquibase(LIQUIBASE_CHANGELOG,
                    new ClassLoaderResourceAccessor(),
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection)));
        } catch (DatabaseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void launchMigrations(Connection connection) {
        try {
            initLiquibase(connection).update();
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
