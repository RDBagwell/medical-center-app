# Journal

What I learned, lesson by lesson, in my own words: the concept, how PHP/Laravel handles it, and how Java handles it.

---

<!-- Template for each entry:

## Lesson N — Title (YYYY-MM-DD)

**In PHP/Laravel:**

**In Java:**

-->
## Lesson 1 Reproducible Builds 2026-10-10
*Concept:* Reproducible builds. So any anybody with access to the repo has one standard to build the project.

pom.xml Project Object Model. Is an XML file that serves as the core configuration file and "single source of truth" for a Maven-based Java project.

In PHP/Laravel: composer.json composer install

In Java: pom.xml mvn compile

My code never imports MySQL-specific classes directly. Instead, database.java uses Java's built-in JDBC classes to connect to the database, so the MySQL driver only needs to be there when the program runs. I left org.json at the default compile scope because App.java uses its classes directly. When I ran the program, I got an "Unknown database 'central_db'" error instead of a driver error. That helped me understand that the MySQL driver was loading correctly, and the problem was that the database couldn't be found.

## Lesson 2 A reproducible database 2026-11-10
*Concept:* schema as code; environments anyone can recreate.

This focused more on building a reproducible seeder database. 
In PHP/Laravel and Java / Flyway: migrations / seeders.

First I created a docker-compose.yml to create a MYSQL container to run the database.

| Feature | PHP / Laravel | Java / Flyway |
|---|---|---|
| **Create or change tables** | Migration class | Usually a SQL migration file |
| **Insert initial data** | Dedicated seeder class | Often SQL `INSERT` statements in migrations |
| **Run database changes** | `php artisan migrate` | Flyway's `migrate` operation |
| **Run seeders** | `php artisan db:seed` | No direct equivalent built into Flyway |
| **Track applied migrations** | Laravel migrations table | `flyway_schema_history` table |
| **Run again** | Seeders can be run again, depending on their logic | Applied versioned migrations normally run only once |

Think of Laravel as providing two separate tools for two related jobs:
- Migrations: Build or modify the database.
- Seeders: Populate the database with the initial data the application needs.
  With Java and Flyway, those jobs can be handled by the same migration system. You can create the tables and insert the default roles in separate, numbered SQL files.
  One important detail: Flyway normally runs each versioned migration only once. If you later add another role, you generally create a new migration rather than editing a migration that's already been applied to a permanent environment.

Product Documentation
+1



That leads to one final distinction. In Laravel, it's easy to rerun a seeder when setting up a development database. With Flyway, you need to decide whether the data is a one-time database change, permanent reference data, or disposable test data. Each may be best handled differently.
The bottom line: Laravel provides separate, ready-made workflows for migrations and seeders. Flyway provides a version-controlled workflow for database changes, including data inserts, while leaving you to decide how to organize other data setup and test fixtures.