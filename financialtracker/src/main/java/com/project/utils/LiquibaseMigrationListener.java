package com.project.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class LiquibaseMigrationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LiquibaseUtils.launchMigrations();


    }
}

