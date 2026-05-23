# Famous ToDo List API

A RESTful task-management API built with **Spring Boot 4.0** and **Java 17**, demonstrating production-ready patterns: layered architecture, input validation, comprehensive test coverage, Docker containerisation, and a fully automated CI/CD pipeline.

> 🌐 **Live demo:** [https://famous-todolist.onrender.com](https://famous-todolist.onrender.com/) ⚠️ Free tier — **allow ~60 seconds** on the first request (cold start).
 
>_Better instructions on how to use live demo in the end of this file_
---

## Tech Stack

|Layer|Choice|
|---|---|
|Language|Java 17|
|Framework|Spring Boot 4.0.6 (Web MVC + Spring Data JPA)|
|ORM|Hibernate / Jakarta Persistence|
|Database|H2 (in-memory)|
|Boilerplate|Lombok|
|Build|Gradle 8 + JaCoCo|
|Testing|JUnit 5, Mockito, MockMvc, @DataJpaTest|
|Container|Docker multi-stage (eclipse-temurin:17-jre-alpine)|
|CI/CD|GitHub Actions → GHCR → Render.com|

---

## Architecture

```
HTTP Request
     │
     ▼
TaskController       (@RestController)
     │
     ▼
TaskService          (@Service — validation, sanitisation, logging)
     │
     ▼
TaskRepository       (JpaRepository<Task, Long>)
     │
     ▼
H2 Database          (in-memory)
```

### Domain Model

|Field|Type|Rules|
|---|---|---|
|`id`|`Long`|Auto-generated (IDENTITY)|
|`description`|`String`|Required, max 200 chars, `<>` stripped|
|`status`|`Status` enum|`PENDING` \| `DOING` \| `DONE` \| `DELETED`|

---

## API Reference

Base URL (local): `http://localhost:8080` Base URL (live): `https://famous-todolist.onrender.com`

|Method|Endpoint|Description|Response|
|---|---|---|---|
|`POST`|`/tasks`|Create a task|201, 400|
|`GET`|`/tasks`|List all tasks|200, 204|
|`GET`|`/tasks/{id}`|Get task by ID|200, 404|
|`GET`|`/tasks/status?status=`|Filter by status|200, 204|
|`GET`|`/tasks/search?keyword=`|Search by keyword (case-insensitive)|200, 204|
|`PUT`|`/tasks/{id}/status?newStatus=`|Update task status|200, 404|
|`DELETE`|`/tasks/{id}`|Delete a task|200, 404|

### Create a task

```http
POST /tasks
Content-Type: application/json

{
  "description": "Review pull requests",
  "status": "PENDING"
}
```
201 Created
```json
{
  "id": 1,
  "description": "Review pull requests",
  "status": "PENDING"
}
```

**Validation rules:**

- `description` is required and cannot be blank
- `description` max length: 200 characters
- `<` and `>` characters are stripped (basic XSS sanitisation)
- Invalid `status` value → `400 Bad Request`

---

## Running Locally

**Prerequisites:** Java 17, Git

```bash
git clone https://github.com/wcampos-dev/famous.todolist
cd famous.todolist
./gradlew bootRun
```

- API: [http://localhost:8080](http://localhost:8080/)
- H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
    - JDBC URL: `jdbc:h2:mem:testdb` | User: `sa` | Password: _(empty)_

### Run Tests

```bash
./gradlew test
```

Coverage report: `build/reports/jacoco/test/html/index.html`

---

## Docker

Multi-stage build — final image uses `eclipse-temurin:17-jre-alpine` and runs as a non-root user.

```bash
docker build -t famous-todolist .
docker run -p 8080:8080 famous-todolist
```

JVM is tuned for low-memory free-tier hosts:

```
-Xmx300M -Xms200M -Xss512K -Djava.security.egd=file:/dev/./urandom
```

---

## Testing

Three complementary test scopes following the testing pyramid:

|Class|Scope|Description|
|---|---|---|
|`TaskControllerTest`|`@SpringBootTest` + MockMvc|HTTP routing, status codes, request/response payloads|
|`TaskServiceTest`|Mockito unit test|Validation, sanitisation, exception paths|
|`TaskRepositoryTest`|`@DataJpaTest`|Custom JPQL queries against real in-memory H2|

JaCoCo enforces a **70% instruction coverage** gate on every build.

---

## CI / CD Pipeline

### ci.yaml — runs on every push

Checkout → JDK 17 setup → Gradle build → JUnit tests → JaCoCo report artifact

### cd.yaml — release automation

|Job|Trigger|Steps|
|---|---|---|
|`release-candidate`|PR to `release/*`|Version script → `rc-vX.Y.Z` tag → Gradle build → push tag|
|`production-release`|Push to `main`|Version script → Gradle build → Docker push to GHCR → Render deploy hook|

Versioning is handled by a shell script (`release-version.sh`) in another private repo that auto-increments the PATCH component from the latest matching git tag.

---

## Live on Render.com

The API is deployed and publicly accessible. Try it with curl:

```bash
curl -X POST https://famous-todolist.onrender.com/tasks \
     -H "Content-Type: application/json" \
     -d '{"description": "My first remote task", "status": "PENDING"}'
```

> ⚠️ The free tier spins down after inactivity. The first request may **take ~60 seconds** — subsequent ones will be fast.

---

## Project Structure

```
famous.todolist/
├── .github/workflows/
│   ├── ci.yaml              # Build & test on every push
│   └── cd.yaml              # Release candidate + production release
├── src/main/java/dev/wcampos/famous/todolist/
│   ├── controller/          # TaskController.java
│   ├── model/               # Task.java, Status.java
│   ├── repository/          # TaskRepository.java
│   ├── service/             # TaskService.java
│   └── Application.java
├── src/main/resources/
│   └── application.properties
├── src/test/                # Controller, Service, Repository tests
├── Dockerfile
├── build.gradle
└── settings.gradle
```

---

## Author

**wcampos-dev** · [github.com/wcampos-dev](https://github.com/wcampos-dev)