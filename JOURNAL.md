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

## Lesson 4 SQL injection 2026-10-15
*Concept:* Never mix code and data.
This fixed SQL injection in the code. Instead of concatenating SQL strings with data, we used a PreparedStatement. This prevents SQL injection by binding the variable to the statement. getUser: "user_name = ?" with userName as the value. This is standard for all code. This was a gotcha for me Varargs is a false friend: Object... looks like the JS spread operator, but it is something different. Inside the method, it’s a plain array; zero arguments gives an empty array, and an existing array passes straight through. There’s no spread operator in Java; Java instead uses a List toArray.

### Java-Specific Things I Learned

I learned that prepared statements only let me bind values, not table or column names. If those names come from user input, I need to use an allowlist instead of trying to escape them.

I also learned that Java's `Object...` works differently than I first expected. It looks similar to JavaScript's spread syntax, but inside the method, it is just an array. If I pass no arguments, it creates an empty array, and if I pass an existing array, Java can use that array directly. Java doesn't have a spread operator like JavaScript, so if I have a `List`, I need to use `toArray()`.

Another thing I learned is that Java doesn't support default parameters like some other languages do. Instead, I can use method overloading to create different versions of a method with different parameters.

I also ran into problems calling instance methods from static methods. Java doesn't allow that directly because a static method doesn't have access to `this`, which helped me understand the difference between static and instance methods.

I learned to keep variables local whenever possible. For example, I should keep a `PreparedStatement` local to the method that uses it instead of making it a class field. That way, one query can't accidentally overwrite another query's statement. If I have repeated code, I should create a helper method rather than use a shared field.

I also learned that SQL errors inside Java strings often aren't caught until runtime. Mistakes like missing spaces in SQL, an incorrect ternary operator, or forgetting to bind a parameter can cause problems when the code actually runs. Because of that, I need to test everything again after making changes or refactoring.

### How This Connects to Laravel

I can apply a lot of what I learned in Java to Laravel. For example, Eloquent's `where('col', $val)` and `DB::select('... = ?', [$val])` safely bind values for me.

However, methods like `DB::raw`, `whereRaw()` with user input directly inside the SQL string, and `orderByRaw($input)` can introduce SQL injection vulnerabilities if I don't handle them carefully.

My Java `select(table, cols, clause, ord, params...)` method is similar to Laravel's `whereRaw('user_name = ?', [$name])` because both allow me to build queries while keeping user-provided values separate from the SQL itself. This helped me understand that the same SQL security principles apply in both languages, even though the syntax is different.

## Lesson 5 Resource lifecycles. 2026-10-18
*Concept:* deterministic cleanup of connections, files and sockets.

This focused on closing open threads and containing memory leaks.
PHP/Laravel mostly handles this automatically, so you rarely have to think about it.
Java handles it with try-with-resources, AutoCloseable, and returning records instead of ResultSet.
The try-with-resources statement automates resource management, eliminating the need for manual, error-prone try-catch blocks in finally.
AutoCloseable acts as a close() method within a try-with-resources statement.
ResultSet leaves an open pointer; records eliminate that problem. So instead of looping through a ResultSet, you can now handle it with an if statement. e.g. f (result.isEmpty()) {…}

## Lesson 6 Resource lifecycles. 2026-10-19
*Concept:* hashing vs. encryption, salts, slow hashes, constant-time comparison.

This whole lesson was about safely storing passwords and keeping them secure.
Hashing is one way and can not be undone. Encryption is faster but it can be reversed.
It is best to use hashing. Salt is an extra layer of protection. 
The old code used sha256 in was hardened by using Bcrypt.
