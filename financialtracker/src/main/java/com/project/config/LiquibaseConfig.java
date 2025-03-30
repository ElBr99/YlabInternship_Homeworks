package com.project.config;

import com.project.utils.ConnectionManager;
import jakarta.annotation.PostConstruct;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LiquibaseConfig {

    @Value("${db.liquibase.changelog}")
    private String liquibaseChangelog;

    @Value("${db.liquibase.defaultSchema}")
    private String liquibaseDefaultSchema;

    @Value("${db.liquibase.mainSchema}")
    private String liquibaseMainSchema;

    private final ConnectionManager connectionManager;

    @SneakyThrows
    @PostConstruct
    public void initLiquibase() {
        var database = DatabaseFactory
                .getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connectionManager.get()));

        database.setLiquibaseSchemaName(liquibaseDefaultSchema);
        database.setDefaultSchemaName(liquibaseMainSchema);

        new Liquibase(
                liquibaseChangelog,
                new ClassLoaderResourceAccessor(),
                database
        ).update("");
    }
}
