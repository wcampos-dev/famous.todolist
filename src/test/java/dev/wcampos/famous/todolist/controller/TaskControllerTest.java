package dev.wcampos.famous.todolist.controller;


import dev.wcampos.famous.todolist.repository.TaskRepository;
import dev.wcampos.famous.todolist.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void cleanDatabase() {
        taskRepository.deleteAll();
    }

    // **Create**

    @Test
    void shouldCreateTask() throws Exception {

        // Added to Utils to be re-usable
        TestUtils.createTask(mockMvc);

    }

    @Test
    void shouldReturnBadRequestWhenTaskIsInvalid() throws Exception {
        // Here we send a JSON without description and invalid status
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"INVALID\"}"))
                //.andExpect(status().isBadRequest()
                .andExpect(status().isBadRequest()
                );
    }


    // **List**

    @Test
    void shouldListAllTasks() throws Exception {

        // Instantiate one task prior to try ti list it
        TestUtils.createTask(mockMvc);

        // List test
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldReturnNoContentWhenNoTasksExist() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnTaskById() throws Exception {
        // Create task
        TestUtils.createTask(mockMvc);

        //Get actual id
        Long id = TestUtils.getIdFromTask(mockMvc);


        mockMvc.perform(get("/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("My New Task"));
    }

    @Test
    void shouldReturnNotFoundWhenNoIdTaskExist() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnTaskByStatus() throws Exception {
        //Create Task with Status PENDING
        TestUtils.createTask(mockMvc);

        //Search for PENDING status
        mockMvc.perform(get("/tasks/status")
                .param("status","PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("My New Task"))
                .andExpect(jsonPath("$[0].status").value("PENDING")
                );
    }

    @Test
    void shouldReturnNoContentWhenNoTaskWithStatus() throws Exception {
        mockMvc.perform(get("/task/status")
                .param("status", "PENDING"))
                .andExpect(status().isNotFound()
                );
    }

    @Test
    void shouldReturnTaskByKeyword() throws Exception {
        // Create a new Task containing the keyword "Important"
        TestUtils.createTask(mockMvc, "My New Important Task", "PENDING");

        // Search for keyword
        mockMvc.perform(get("/tasks/search")
                .param("keyword", "Important"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("My New Important Task"))
                .andExpect(jsonPath("$[0].status").value("PENDING")

                );
    }

    @Test
    void shouldReturnNoContentWhenKeywordNotFound() throws Exception {
        mockMvc.perform(get("/tasks/search")
                .param("keyword", "Important"))
                .andExpect(status().isNoContent());
    }

    // **Update**

    @Test
    void shouldUpdateTaskStatus() throws Exception {
        //Create a PENDING status task
        TestUtils.createTask(mockMvc);

        //Get actual Id
        Long id = TestUtils.getIdFromTask(mockMvc);

        //Update status to DOING
        mockMvc.perform(put("/tasks/" + id + "/status")
                .param("newStatus", "DOING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DOING"));

    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingTask() throws Exception {
        mockMvc.perform(put("/tasks/1/status")
                .param("newStatus", "DOING"))
                .andExpect(status().isNotFound());
    }

    // **Delete**

    @Test
    void shouldDeleteTaskSuccessfully() throws Exception {
        // Create a new Task to delete later on
        TestUtils.createTask(mockMvc);

        Long id = TestUtils.getIdFromTask(mockMvc);

        // Delete created Task (Id = 1)
        mockMvc.perform(delete("/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string("Task successfully deleted!"));

    }

    @Test
    void shouldReturnNotFountWhenDeletingNonExistingTask() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found :("));
    }



}
