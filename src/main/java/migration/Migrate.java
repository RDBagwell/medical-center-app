package migration;

import config.AppConfig;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.output.MigrateResult;

import java.util.ArrayList;
import java.util.List;

public final class Migrate {
    public static void main(String[] args) {
        List<String> locations = new ArrayList<>();

        locations.add("classpath:db/migration");
        if (AppConfig.SEED_DEV_DATA) {
            locations.add("filesystem:db/dev-seed");
        }

        Flyway flyway = Flyway.configure().dataSource(
                AppConfig.DB_URL,
                AppConfig.MYSQL_USER,
                AppConfig.MYSQL_PASSWORD
        ).locations(locations.toArray(new String[0])).load();

        MigrateResult result = flyway.migrate();

        MigrationInfo current = flyway.info().current();

        String schemaVersion = current != null
                && current.getVersion() != null
                ? current.getVersion().toString()
                : "none";

        System.out.println("Flyway migration completed.");
        System.out.println("Locations used: " + locations);
        System.out.println("Migrations executed: "
                + result.migrationsExecuted);
        System.out.println("Current schema version: "
                + schemaVersion);
    }

    private Migrate() {
    }
}
