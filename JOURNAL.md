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

## Lesson 2 A reproducible database 2026-10-11
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

## Lesson 3 Configuration and secrets 2026-10-13
*Concept:* config is not code; secrets never go in the repo.
This Lesson was all about keeping config vars out of code. Java doesn't read .env files natively, unlike Laravel.
I created an AppConfig and Migrate to handle database setup, connection, and seeding without the need to set $evn varables in the terminal. In the pom.xml, I added Flyway as a dependency rather than having it in the plugins. And set exec.mainClass to run the app in one command.

Laravel uses the .env file to store settings such as database credentials and application names. The env() function reads these environment values, usually inside configuration files like config/app.php or config/database.php, where Laravel uses them to build its configuration array. The config() function then retrieves those loaded settings anywhere in your application.