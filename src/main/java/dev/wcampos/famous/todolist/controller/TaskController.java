package dev.wcampos.famous.todolist.controller;

import dev.wcampos.famous.todolist.model.Status;
import dev.wcampos.famous.todolist.model.Task;
import dev.wcampos.famous.todolist.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // **Create new Task**

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task newTask = taskService.saveTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    // **List**

    // All
    @GetMapping
    public ResponseEntity<List<Task>> listTasks(){
        List<Task> tasks = taskService.findAll();

        return getListResponseEntity(tasks);
    }

    // By Id
    @GetMapping("/{id}")
    public Task listById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    // By Status
    @GetMapping("/status")
    public ResponseEntity<List<Task>> listByStatus(@RequestParam Status status) {
        List<Task> tasks = taskService.findByStatus(status);

        return getListResponseEntity(tasks);

    }

    // By Keyword
    @GetMapping("/search")
    public ResponseEntity<List<Task>> findByDescriptionContaining(@RequestParam String keyword){
        List<Task> tasks = taskService.findByDescriptionContaining(keyword);

        return getListResponseEntity(tasks);
    }

    private static ResponseEntity<List<Task>> getListResponseEntity(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(tasks);
    }

    // **Update**

    // Status
    @PutMapping("/{id}/status")
    public Task updateStatus(@PathVariable Long id, @RequestParam Status newStatus){
        return taskService.updateStatus(id, newStatus);
    }

    // **Delete**

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        boolean success = taskService.deleteTask(id);

        if (success){
            // If the task existed and was deleted.
            return ResponseEntity.ok("Task successfully deleted!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Task not found :(");
        }

    }

}
