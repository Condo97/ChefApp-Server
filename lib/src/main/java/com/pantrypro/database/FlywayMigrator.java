package com.pantrypro.database;

import org.flywaydb.core.Flyway;

/**
 * Runs Flyway database migrations on startup.
 * Migrations are located in classpath:db/migration.
 * Uses baselineOnMigrate to handle existing schemas that predate Flyway.
 */
public class FlywayMigrator {

    public static void migrate(String jdbcUrl, String user, String password) {
        Flyway flyway = Flyway.configure()
                .dataSource(jdbcUrl, user, password)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
    }

}
