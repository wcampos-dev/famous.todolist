package dev.wcampos.famous.todolist.util;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUtils {

    // Helper to create Tasks by MockMvc

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
//                .andExpect(jsonPath("$.description").value("My New Task"))
//                .andExpect(jsonPath("$.status").value("PENDING")
//                );

    }


}