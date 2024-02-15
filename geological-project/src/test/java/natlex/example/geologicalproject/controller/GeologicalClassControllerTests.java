
package natlex.example.geologicalproject.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class GeologicalClassControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGeologicalClassController_GetAllGeologicalClasses() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/geological-classes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGeologicalClassController_FindById() throws Exception {
        Long geologicalClassId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/geological-classes/{id}", geologicalClassId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGeologicalClassController_CreateGeologicalClass() throws Exception {
        String className = "Test Class";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/geological-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + className + "\", \"code\":\"TestCode\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGeologicalClassController_UpdateGeologicalClass() throws Exception {
        Long geologicalClassId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.put("/api/geological-classes/{id}", geologicalClassId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Class\", \"code\":\"UpdatedCode\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGeologicalClassController_DeleteGeologicalClass() throws Exception {
        Long geologicalClassId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/geological-classes/{id}", geologicalClassId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

