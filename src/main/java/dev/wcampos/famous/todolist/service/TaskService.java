package dev.wcampos.famous.todolist.service;

import dev.wcampos.famous.todolist.model.Status;
import dev.wcampos.famous.todolist.model.Task;
import dev.wcampos.famous.todolist.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // **Save Task**

    public Task saveTask(Task task) {
        // Validation: Description cannot be empty or null
        if (!StringUtils.hasText(task.getDescription())) {
            throw new IllegalArgumentException("Task description cannot be empty or null.");
        }

        // Validation: Fix maximum size
        if (task.getDescription().length() > 200) {
            throw new IllegalArgumentException("Description cannot be grather than 200 characters");
        }

        // Sanitization: Trim extra spaces and dangerous chars
        String sanityDescription = task.getDescription()
                .trim()
                .replaceAll("[<>]", ""); //Avoid simple html tags

        // Assign new description
        task.setDescription(sanityDescription);

        // Logs
        logger.info("Saving tasking description: {}", sanityDescription);



        return taskRepository.save(task);
    }


    // **List Task**

    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    // By Status
    public List<Task> findByStatus(Status status){
        return taskRepository.findByStatus(status);
    }

    // By Keyword
    public List<Task> findByDescriptionContaining(String keyword){
        return taskRepository.findByDescriptionContaining("%" + keyword.toLowerCase() + "%");
    }

    // By Id
    public Task findById(Long id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not Found"));
    }

    // ** Update **

    //Status

    public Task updateStatus(Long id, Status newStatus){
        Task task = findById(id);
        task.setStatus(newStatus);

        return taskRepository.save(task);
    }


    // **Delete**

    public boolean deleteTask(Long id){
        if (taskRepository.findById(id).isPresent()){
            taskRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


}
