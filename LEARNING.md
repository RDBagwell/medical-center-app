# Learning Roadmap

**Project:** a desktop clinic app (front desk first, with nurses and doctors adding notes), covering patients, appointments, visit notes, prescriptions and billing. It starts as a Java Swing app and migrates to the web.

**Goal:** learn the **universal concepts** behind good software, using Java as the vehicle, and show the skills transfer from PHP/Laravel.

**Pace:** one lesson ≈ one 45-minute session. Some lessons may take two sessions, which is fine. At one lesson a week, this is roughly a year-long path, and every phase leaves the app in a working, showable state.

Each lesson lists the **concept** (the universal idea), the **task** (what you build in this app) and, where useful, the **Laravel parallel**.

Legend: `[ ]` not started · `[~]` in progress · `[x]` done

---

## Phase 1 — Foundations and safety

The existing code works, but it has the classic problems every language has. Fixing them is the first set of lessons.

- [~] **1. Build tools and project layout.** *(In progress: `pom.xml` done, `mvn clean compile` = BUILD SUCCESS on Java 21. Left: `git rm lib/`, `exec-maven-plugin` launch, re-import in IntelliJ from the pom and drop the `.iml`.)* *Concept:* reproducible builds and dependency management. *Task:* a code tour (what's here, what you remember), then convert to a **Maven** project: create `pom.xml`, use the standard `src/main/java` layout, replace the committed `.jar` files in `lib/` with Maven dependencies, and get it compiling and launching. *Laravel parallel:* `composer.json`.
- [ ] **2. A reproducible database.** *Concept:* schema as code; environments anyone can recreate. *Task:* MySQL in `docker-compose.yml`, the schema in a versioned SQL migration (Flyway), and a fake-data seed script. *Laravel parallel:* migrations and seeders.
- [ ] **3. Configuration and secrets.** *Concept:* config is not code; secrets never go in the repo. *Task:* move the hardcoded `root` / `password` connection details to environment variables or an untracked properties file, with a committed example file. *Laravel parallel:* `.env` and `config/`.
- [ ] **4. SQL injection.** *Concept:* never mix code and data; parameterize everything. *Task:* first, prove the login bug (a username of `' OR '1'='1`). Then fix `User.getUser()`, `select()` and `delete()` so every query uses `PreparedStatement`. *Laravel parallel:* why Eloquent and query bindings protect you, and where raw queries don't.
- [ ] **5. Resource lifecycles.** *Concept:* deterministic cleanup of connections, files and sockets. *Task:* fix the connection leaks with **try-with-resources** and `AutoCloseable`. Also fix the two broken statements: `DELETE` missing `FROM`, and `truncate()` calling `executeQuery`. *Laravel parallel:* PHP cleans up at the end of every request, so long-running programs need this discipline.
- [ ] **6. Password storage.** *Concept:* hashing vs. encryption, salts, slow hashes, constant-time comparison. *Task:* hash passwords with **BCrypt** on create and update, verify them at login, and migrate any existing plain-text rows. *Laravel parallel:* `Hash::make` / `Hash::check`.
- [ ] **7. First tests.** *Concept:* automated tests as a safety net; what to test and what not to. *Task:* add **JUnit 5**, then write tests for password hashing and for input validation. Run them with `mvn test`. *Laravel parallel:* PHPUnit / Pest.
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

- **2026-10-10 — Lesson 1 (in progress).** Code tour; reproducible builds and Maven vs. Composer; compile vs. runtime classpath (proved the MySQL driver is only needed at runtime → `runtime` scope). Robert wrote `pom.xml` (`io.github.rdbagwell:medical-center-app`, release 21), renamed the `_` lambda params, and got BUILD SUCCESS from the terminal. **Next:** review the final pom, `git rm lib/`, `exec:java` to launch the window, IntelliJ re-import, then journal entry and tick Lesson 1.
