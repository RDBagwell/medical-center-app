# Learning Roadmap

**Project:** a desktop clinic app (front desk first, with nurses and doctors adding notes), covering patients, appointments, visit notes, prescriptions and billing. It starts as a Java Swing app and migrates to the web.

**Goal:** learn the **universal concepts** behind good software, using Java as the vehicle, and show the skills transfer from PHP/Laravel.

**Pace:** one lesson ≈ one 45-minute session. Some lessons may take two sessions, which is fine. At one lesson a week, this is roughly a year-long path, and every phase leaves the app in a working, showable state.

Each lesson lists the **concept** (the universal idea), the **task** (what you build in this app) and, where useful, the **Laravel parallel**.

Legend: `[ ]` not started · `[~]` in progress · `[x]` done

---

## Phase 1 — Foundations and safety

The existing code works, but it has the classic problems every language has. Fixing them is the first set of lessons.

- [x] **1. Build tools and project layout.** *Concept:* reproducible builds and dependency management. *Task:* a code tour (what's here, what you remember), then convert to a **Maven** project: create `pom.xml`, use the standard `src/main/java` layout, replace the committed `.jar` files in `lib/` with Maven dependencies, and get it compiling and launching. *Laravel parallel:* `composer.json`.
- [x] **2. A reproducible database.** *Concept:* schema as code; environments anyone can recreate. *Task:* MySQL in `docker-compose.yml`, the schema in a versioned SQL migration (Flyway), and a fake-data seed script. *Laravel parallel:* migrations and seeders.
- [x] **3. Configuration and secrets.** *Concept:* config is not code; secrets never go in the repo. *Task:* move the hardcoded `root` / `password` connection details to environment variables or an untracked properties file, with a committed example file. *Laravel parallel:* `.env` and `config/`. *(Done: `config/AppConfig` is the single loader for the app and migrations (env var > `.env` > fail fast; optional `SEED_DEV_DATA` defaults to `false`); `migration/Migrate` runs Flyway via its Java API; the Flyway Maven plugin is gone.)*
- [x] **4. SQL injection.** *Concept:* never mix code and data; parameterize everything. *Task:* first, prove the login bug (a username of `' OR '1'='1`). Then fix `User.getUser()`, `select()` and `delete()` so every query uses `PreparedStatement`. *Laravel parallel:* why Eloquent and query bindings protect you, and where raw queries don't. *(Done: `query(sql, Object... params)` is the single read path (PreparedStatement + `bindParameters`); `select()` takes bound values as varargs; `getUser` binds `user_name = ?`; `delete()` fixed (`FROM`, bound id). `update()`'s `clause` is still a raw string by contract.)*
- [x] **5. Resource lifecycles.** *Concept:* deterministic cleanup of connections, files and sockets. *Task:* fix the connection leaks with **try-with-resources** and `AutoCloseable`. Also fix the two broken statements: `DELETE` missing `FROM`, and `truncate()` calling `executeQuery`. *Laravel parallel:* PHP cleans up at the end of every request, so long-running programs need this discipline. *(Done: write path closes via try-with-resources (`database implements AutoCloseable`, `executeSQL`, `User.add`/`updateUser`); read path returns copies, not cursors: `User` is a record, `getUser` → `Optional<User>`, `getUsers` → `List<User>`, `fromRow` maps rows, both resources in the try header; `toString()` redacts the hash. Verified: 10 failed logins leak 0 connections (Part A: 5 leaked ~4). Removed unused `truncate()`, `SelectUser`, `SearchApp`, `LinkListExample`.)*
- [x] **6. Password storage.** *Concept:* hashing vs. encryption, salts, slow hashes, constant-time comparison. *Task:* hash passwords with **BCrypt** on create and update, verify them at login, and migrate any existing plain-text rows. *Laravel parallel:* `Hash::make` / `Hash::check`. *(Done: `helper/PasswordHasher` wraps `BCryptPasswordEncoder(12)` (`spring-security-crypto` compile + `spring-core` runtime); `AddUser`/`UpdateUser` hash, `checkUser` verifies via `Optional<User>`; SHA-256 code deleted. `update()` is a real UPDATE (refuses a blank WHERE, merges SET and WHERE params). Old rows migrated by regenerating the fake seed: `R__dev_users.sql` holds three distinct precomputed bcrypt hashes.)*
- [~] **7. First tests.** *Concept:* automated tests as a safety net; what to test and what not to. *Task:* add **JUnit 5**, then write tests for password hashing and for input validation. Run them with `mvn test`. *Laravel parallel:* PHPUnit / Pest.
- [ ] **8. Conventions and clean-up.** *Concept:* code is read far more than it's written. *Task:* Java naming conventions (`database` becomes `Database`; lowercase packages), delete the scratch code (`Junk.java`, `LinkListExample.java` and similar), add a proper `.gitignore`, and keep methods small.

**Checkpoint:** a safe, tested, buildable version of the original app.

## Phase 2 — Architecture

- [ ] **9. Layers and separation of concerns.** *Concept:* the UI shouldn't know about SQL. *Task:* split into UI → service → repository, and move all SQL out of the Swing panels. *Laravel parallel:* controllers vs. services vs. models.
- [ ] **10. Interfaces and the repository pattern.** *Concept:* depend on abstractions, not implementations. *Task:* a `UserRepository` interface with a JDBC implementation. *Laravel parallel:* contracts.
- [ ] **11. Immutability and value objects.** *Concept:* data that can't change can't surprise you. *Task:* Java **records** for data classes, and value objects for things like an email address or a phone number.
- [ ] **12. Representing absence.** *Concept:* null is a design decision. *Task:* `Optional` for lookups that may find nothing, and removing null-returning methods.
- [ ] **13. Errors.** *Concept:* handle errors at the boundaries; fail loudly and clearly. *Task:* checked vs. unchecked exceptions, custom domain exceptions, and friendly error dialogs in the UI instead of stack traces. *Laravel parallel:* the exception handler.
- [ ] **14. Dependency injection by hand.** *Concept:* inversion of control and a single place where objects are wired together. *Task:* constructor injection plus a "composition root" in `main`, with no framework. *Laravel parallel:* the service container. Doing it by hand first is how you understand what the container does.
- [ ] **15. Test doubles.** *Concept:* isolate what you're testing. *Task:* an in-memory repository, so services can be tested without a database.

**Checkpoint:** an architecture that's ready for real features, and later for the web.

## Phase 3 — Patients and access

- [ ] **16. Domain modeling and invariants.** *Concept:* objects that can't be put into an invalid state. *Task:* a `Patient` with validated fields (name, date of birth, contact details, insurance). It can't be created invalid.
- [ ] **17. Collections and streams.** *Concept:* data transformation pipelines. *Task:* a patient search screen with filtering and sorting using streams and `Comparator`. *Laravel parallel:* collections.
- [ ] **18. Dates and times.** *Concept:* time is harder than it looks. *Task:* `java.time` for dates of birth, age, appointment times and time zones. Learn why `LocalDate`, `LocalDateTime` and `Instant` all exist. *Laravel parallel:* Carbon.
- [ ] **19. Enums with behavior.** *Concept:* making illegal states unrepresentable. *Task:* a `Role` enum (front desk, nurse, doctor, admin) that carries its own permissions.
- [ ] **20. Authorization.** *Concept:* authentication vs. authorization; least privilege. *Task:* role-based checks on what each role can see and do, enforced in the service layer and not just by hiding buttons. *Laravel parallel:* gates and policies.
- [ ] **21. Audit trail.** *Concept:* accountability and append-only records. *Task:* log who viewed or changed which patient record and when. In medical software this isn't optional.

## Phase 4 — Appointments

- [ ] **22. State machines.** *Concept:* explicit states and allowed transitions. *Task:* an appointment lifecycle (scheduled → checked in → in progress → completed, plus cancelled and no-show), with illegal transitions rejected and tested.
- [ ] **23. Interval logic.** *Concept:* algorithms and edge cases. *Task:* detect overlapping appointments for a doctor or room, with thorough tests at the boundaries.
- [ ] **24. Transactions and races.** *Concept:* atomicity, isolation and race conditions. *Task:* prevent double-booking when two front-desk users book the same slot at once, using database transactions and constraints. *Laravel parallel:* `DB::transaction`.
- [ ] **25. UI models and the observer pattern.** *Concept:* model–view separation; events. *Task:* a daily schedule screen using `JTable` with a custom `TableModel` that updates itself when data changes.
- [ ] **26. Concurrency basics.** *Concept:* never block the UI thread. *Task:* move slow database work off the Swing thread with `SwingWorker`, with a loading state and error handling.
- [ ] **27. End-to-end flow.** *Task:* patient check-in from the schedule screen. Pull Phases 3 and 4 together.

## Phase 5 — Clinical records

- [ ] **28. Integrity of records.** *Concept:* immutable history; amend rather than overwrite. *Task:* visit notes by nurses and doctors with author and timestamp. Corrections are amendments that keep the original.
- [ ] **29. Structured data and validation.** *Task:* prescriptions with structured dosage, frequency and duration, validated as a value object, not free text.
- [ ] **30. Polymorphism.** *Concept:* one interface, many behaviors. *Task:* different note types (triage, visit, follow-up) behind one interface, with sealed types and pattern matching.
- [ ] **31. Performance.** *Concept:* indexes, the N+1 query problem and pagination. *Task:* make patient history load quickly with a lot of fake data. Measure before and after. *Laravel parallel:* eager loading.

## Phase 6 — Billing

- [ ] **32. Representing money.** *Concept:* never use floating point for money. *Task:* `BigDecimal` (or integer cents), with rounding rules and a `Money` value object.
- [ ] **33. Aggregates.** *Concept:* consistency boundaries. *Task:* invoices with line items whose totals can never disagree with their lines.
- [ ] **34. Payments and idempotency.** *Concept:* doing something twice must not charge twice. *Task:* record payments and balances safely.
- [ ] **35. Reporting.** *Concept:* grouping and aggregation. *Task:* daily and monthly revenue reports with stream collectors.
- [ ] **36. File I/O and encoding.** *Concept:* bytes vs. characters; encodings. *Task:* export invoices and reports to CSV (and optionally PDF).

**Checkpoint:** a complete desktop clinic app. Good time to write the README and record a demo.

## Phase 7 — To the web

- [ ] **37. Modularity.** *Concept:* a core that doesn't care about its UI. *Task:* split into a multi-module Maven build: `core` (domain and services), `desktop` (Swing). The payoff of Phase 2.
- [ ] **38. Frameworks and inversion of control.** *Task:* add a **Spring Boot** module that reuses `core`, and compare it to the hand-wired composition root from Lesson 14. *Laravel parallel:* this is the closest Java gets to Laravel.
- [ ] **39. API design.** *Concept:* resources, status codes, DTOs vs. domain objects. *Task:* a REST API for patients and appointments.
- [ ] **40. ORMs and their trade-offs.** *Task:* Spring Data JPA compared with your hand-written JDBC repositories: when each is the better choice. *Laravel parallel:* Eloquent.
- [ ] **41. Web security.** *Task:* Spring Security, sessions vs. tokens, and role checks on the API. *Laravel parallel:* Sanctum and policies.
- [ ] **42. Validation and error responses** for the API.
- [ ] **43. Integration tests** with a real database in a container (Testcontainers).
- [ ] **44. A web front end.** *Task:* a React front end for the front desk. This is your home turf, so it goes fast. Then decide the desktop app's future: retire it, or keep it as a second client of the same core.

**Capstone:** CI with GitHub Actions, a polished README telling the "same concepts, two languages" story, and `docs/JOURNAL.md` as the record of what you learned.

---

## Session log

Newest entry first. One or two lines per session: date, lesson, what got done, the next step.

- **2026-10-24 — Lesson 7 Part A done (in progress).** JUnit 6.1.3 (`junit-jupiter`, test scope) + Surefire pinned to 3.6.0. `src/test/java/helper/PasswordHasherTest`: 7 behavior-named tests (verifies own hash, wrong password, two hashes differ and both verify, `$2a$12$` prefix, 60 chars, rejects >72 bytes, rejects 40×`é` = 80 bytes). Proved the net: a deliberately wrong assertion failed, and `COST = 10` failed only `usesBcryptAtCost12` (run time 2.0 s → 0.6 s). Learned: Maven lifecycle (`test` runs compile first; compile errors = 0 tests), Surefire finds classes by name pattern, Failure vs Error, one behavior per test (not one assertion), names as bug reports, bytes vs `String.length()`. **Next:** Part B — a pure input validator (blank username/password, password ≤ 72 UTF-8 bytes) written test-first, wired into `AddUser`/`UpdateUser` so the 72-byte exception never reaches Swing; then journal and tick Lesson 7.
- **2026-10-19 — Lesson 6 done.** Part B: BCrypt wired into add/update/login; `update()` fixed (no UPDATE without WHERE, `isBlank` guard, callers pass only the condition, SET + WHERE params merged with `Arrays.copyOf`/`System.arraycopy`). Verified: new users get `$2a$12$`, update changed only its own row (new salt), SHA-256 `test.frontdesk` rejected as predicted, bcrypt login works. Part C: migration strategies (force reset / rehash-on-login / regenerate) — hashes are one-way, so fake data is regenerated: seed now has three distinct bcrypt hashes; Flyway reran the `R__` because its checksum changed (a changed `V__` checksum fails validation instead); `DELETE` kept `AUTO_INCREMENT` going (ids 12–14, predicted). `test.frontdesk` logs in again. Learned: `NoClassDefFoundError` = missing at runtime, `mvn dependency:tree`, no milestone versions. **Next:** Lesson 6 journal entry, then Lesson 7 (JUnit 5: tests for `PasswordHasher` first). Optional stretches open: rehash-on-login, dummy-hash timing path in `checkUser`, columns/vals length guard.
- **2026-10-18 — Lesson 5 done.** Part C: `User` became a record with static finders; `getUser` returns `Optional<User>`, `getUsers` returns `List<User>`, rows copied via `fromRow` inside try-with-resources (both `database` and `ResultSet` in the header), so no cursor leaves the model. Callers updated (`Helper.checkUser` with a guard clause, `AddUser`/`UpdateUser` via `isEmpty()`, `App` owns its `db`); no `ResultSet` outside `Model`/`database`. Overrode `toString()` to keep the password hash out of logs. Deleted dead prototypes (`SelectUser`, `SearchApp`, `LinkListExample`). Caught and fixed: `while (isPresent())` infinite loop, inverted `isPresent`/`isEmpty`, `rs.getRow()` vs the `id` column. Verified: `Threads_connected` 2 → 2 after 10 failed logins; login, add, duplicate check, 51 states all work. Learned: records, `Optional`, cursor model (`next()`), return copies not handles, whole-module compilation. **Next:** Lesson 5 journal entry, then Lesson 6 (BCrypt: salted slow hashes, constant-time compare, charset). Noted for Lesson 8: `UpdateUser` is a copy of `AddUser` (`User.updateUser` has no caller).
- **2026-10-17 — Lesson 5 Part B done.** Measured the leak (Part A) with `SHOW STATUS LIKE 'Threads_connected'`. Write path now closes deterministically: `AutoCloseable` + try-with-resources in `executeSQL`, `User.add`, `User.updateUser`; removed catch-and-rethrow (it lost MySQL's error code); constructor no longer swallows (DB-down now fails at `database.<init>` with `Connection refused`); `con` back to an instance field; `truncate()` deleted. Learned: try-with-resources = try/finally (no catch needed), resource variable is scoped to the block, catch only to recover/translate/report. **Next:** Part C: `getUser`/`getUsers`/`App` copy rows into Java objects inside the try and return those (ownership: whoever opens closes); then `query()`'s statement; verify login leaks 0 connections; journal and tick Lesson 5.
- **2026-10-15 — Lesson 4 done.** Proved the login bypass (`' OR '1'='1` + a known password opened the main panel; `LoginGUI` now mounts `loginPanel`). Fixed it with `PreparedStatement`: new `query(sql, Object... params)`, `select()` passes values as varargs, extracted `bindParameters`/`setParameter` (static; `setObject` fallback instead of silently skipping unknown types), `getUser` binds `user_name = ?`, `delete()` gets `FROM` + a bound id. Learned varargs (rest, not spread; Java has no defaults, it uses overloads) and identifiers vs. values (`?` can't bind table/column names, so use an allowlist). Verified: injection rejected, valid/invalid logins, 51 states, insert and delete of a `test.` user. **Next:** Lesson 4 journal entry, then Lesson 5 (resource lifecycles: the un-closed `PreparedStatement`s, the swallowing constructor, `truncate()`).
- **2026-10-13 — Lesson 3 done.** Part B: Flyway now runs from Java (`migration/Migrate`, via `mvn compile exec:java "-Dexec.mainClass=migration.Migrate"`) using `AppConfig`, so there's one config source and it works from a fresh terminal. Refactored `AppConfig` into `lookup` / `require` / `optionalBoolean(key, default)`. Pom: removed the Flyway plugin, added `flyway-core` (compile) and `flyway-mysql` (runtime), and moved the default main class to the `exec.mainClass` property (learned that `<configuration>` silently beats `-D`). Saw that turning the seed flag off on a seeded DB fails validation ("applied migration not resolved locally"); decided on `down -v` rather than weakening validation. Verified: fresh DB unseeded = 4 migrations and 0 users; seed flag on = the R__ applied and 3 users. **Next:** Lesson 3 journal entry, then Lesson 4. Noticed the three test users share one identical unsalted SHA-256 hash, which the password-hashing lesson will fix.
- **2026-10-13 — Lesson 3 Part A done.** Built `AppConfig` (precedence: env var > `.env` > `IllegalStateException` naming the key); app now reads config and printed 51 states from the 3308 container. Found DC missing from V3 via the JSON output; first attempt edited V3 (shared, merged) — redid it forward-only as V4, and fixed V4 in place while it was still unpushed. Fail-fast verified. Flagged: old `root`/`password` stays in git history; rotate the work stack's MySQL root password if it matches. **Next:** Part B: Flyway reads the same `.env` (one config source for app, Compose and migrations); then journal and tick Lesson 3.
- **2026-10-12 — Lesson 2 done.** Part B: reference vs. dev data. `V3__insert_states.sql` (51 rows, versioned, ships to prod); `db/dev-seed/R__dev_users.sql` outside `src/main` (repeatable, idempotent DELETE+INSERT on the `test.` marker, SHA2 hashes). Verified: no-op rerun, prod path = 0 users, dev path = 3 users, both from an empty volume. **Next:** journal entry for Lesson 2, then Lesson 3: one config source so the app, Flyway and Compose stop duplicating credentials, and connect the app to 3308.
- **2026-10-11 — Lesson 2 Part A done.** Schema as code: dedicated MySQL container (pinned, port 3308, own volume, credentials in an untracked `.env`), Flyway plugin with credentials passed via env vars, V1 `users` and V2 `states`. Saw the checksum mismatch after editing applied migrations; learned why `repair` would hide drift; rebuilt with `down -v` + migrate. **Next:** Part B: fake seed data (where it belongs vs. migrations), then connect the app to 3308; then journal entry and tick Lesson 2.
- **2026-10-11 — Lesson 1 done.** Added `runtime` scope for the driver, removed `lib/`, `.iml` and `pop.xml`, added `exec-maven-plugin` (`mvn compile exec:java` launches `servlet.LoginGUI`). Saw the driver reach MySQL ("Unknown database 'central_db'") and the swallowed-exception NPE. Found the shared `docker-mysql` (`mysql:latest`, port 3307) has no `central_db`. **Next:** Lesson 2: docker-compose with a dedicated, pinned MySQL; Flyway; fake seed data.
- **2026-10-10 — Lesson 1 (in progress).** Code tour; reproducible builds and Maven vs. Composer; compile vs. runtime classpath (proved the MySQL driver is only needed at runtime → `runtime` scope). Robert wrote `pom.xml` (`io.github.rdbagwell:medical-center-app`, release 21), renamed the `_` lambda params, and got BUILD SUCCESS from the terminal. **Next:** review the final pom, `git rm lib/`, `exec:java` to launch the window, IntelliJ re-import, then journal entry and tick Lesson 1.
