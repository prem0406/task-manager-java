package com.example.task_manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class OpenApiDocumentationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    void exposesDocumentedTaskApi() throws Exception {
        MockMvc mockMvc = webAppContextSetup(webApplicationContext).build();

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Task Manager API"))
                .andExpect(jsonPath("$.paths['/api/tasks'].get").exists())
                .andExpect(jsonPath("$.paths['/api/tasks/{id}'].get").exists());
    }
}