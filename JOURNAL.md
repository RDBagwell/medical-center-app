# Journal

What I learned, lesson by lesson, in my own words: the concept, how PHP/Laravel handles it, and how Java handles it.

---

<!-- Template for each entry:

## Lesson N — Title (YYYY-MM-DD)

**In PHP/Laravel:**

**In Java:**

-->
Lesson 1 Reproducible Builds 2026-10-10
Concept: Reproducible builds. So any anybody with access to the repo has one standard to build the project.

pom.xml Project Object Model. Is an XML file that serves as the core configuration file and "single source of truth" for a Maven-based Java project.

In PHP/Laravel: composer.json composer install

In Java: pom.xml mvn compile

My code never imports MySQL-specific classes directly. Instead, database.java uses Java's built-in JDBC classes to connect to the database, so the MySQL driver only needs to be there when the program runs. I left org.json at the default compile scope because App.java uses its classes directly. When I ran the program, I got an "Unknown database 'central_db'" error instead of a driver error. That helped me understand that the MySQL driver was loading correctly, and the problem was that the database couldn't be found.