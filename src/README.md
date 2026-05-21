# 📘 TodoList API

## 📖 Overview
***

This is a simple Spring Boot application for managing tasks (ToDo List).
It exposes a REST API that allows you to create, list, search, update, and delete tasks.

## 🚀 Technologies

Java 17+

Spring Boot

Spring Data JPA

H2 Database (or another relational database) - Dev Environment

Gradle

## ⚙️ API Endpoints
### Create a task
`http
POST /tasks`
Content-Type: application/json

```JSON
{
"description": "Create API Rest",
"status": "PENDING"
}
```
Response:  
201 Created with the created task object.

### List all tasks
`http GET /tasks`

Response:  
JSON list of tasks.

200 OK → task found

204 No Content → No body returned for response

### Get task by ID
http
GET /tasks/{id}
Response:

200 OK → task found

404 Not Found → task not found

### Search tasks by status (PENDING, DOING, DONE, DELETED)

http
GET /tasks/status?status=PENDING

200 OK → task found

204 No Content → No body returned for response


### Search tasks by keyword

http
GET /tasks/search?keyword=Spring
Update task status
http
PUT /tasks/{id}/status?newStatus=DOING
Response:  
Task object with updated status.

### Delete task
http
DELETE /tarefas/{id}
Response:

200 OK → "Task successfully deleted."

404 Not Found → "Task not found."

## 🛠️ How to run the project
Clone the repository:

```bash
git clone https://github.com/youruser/todolist.git
```

Navigate into the project folder:

```bash
cd todolist
```

Build and run with Gradle:

```bash
./gradlew bootRun
```

## Access the API at:

```Code
http://localhost:8080/tasks
```

## 📌 Notes
The H2 database runs in memory by default.

To access the H2 console:

Code
http://localhost:8080/h2-console