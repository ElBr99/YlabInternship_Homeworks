import utils.ConnectionManager;
import utils.LiquibaseUtils;
import utils.UI;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppRunnerSingleConsoleBased {

    private static final ExecutorService executor = Executors.newFixedThreadPool(1); // Пул потоков

    public static void main(String[] args) {
        LiquibaseUtils.launchMigrations();

        System.out.println("Database migration completed successfully.");

        System.out.println("Многопользовательское консольное приложение (имитация с многопоточностью)");
        executor.submit(UI::start);
    }
}