package dev.wcampos.famous.todolist.util;

import com.jayway.jsonpath.JsonPath;
import dev.wcampos.famous.todolist.model.Status;
import dev.wcampos.famous.todolist.model.Task;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUtils {

    // Helper to create Tasks by MockMvc

    // ** TaskControllerTest helper

    // New Task autofill description and status
    public static void createTask(MockMvc mockMvc) throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"My New Task\",\"status\":\"PENDING\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("My New Task"))
                .andExpect(jsonPath("$.status").value("PENDING")
                );

    }

    // New task requires description and status
    public static void createTask(MockMvc mockMvc, String description, String status) throws Exception {
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"" + description + "\",\"status\":\"" + status + "\"}"))
                .andExpect(status().isCreated());
    }

    public static Long getIdFromTask(MockMvc mockMvc) throws Exception {
        MvcResult result = mockMvc.perform(get("/tasks"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return JsonPath.parse(response).read("$[0].id", Long.class);
    }

    // ** TaskServiceTest helper

    public static Task sampleTask(Long id, String description, Status status) {
        return new Task(id, description, status);
    }




}