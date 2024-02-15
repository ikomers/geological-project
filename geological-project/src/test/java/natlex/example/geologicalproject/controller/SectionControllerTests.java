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
public class SectionControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSectionController_GetAllSections() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/sections"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testSectionController_FindById() throws Exception {
        Long sectionId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/sections/{id}", sectionId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testSectionController_CreateSection() throws Exception {
        String sectionName = "Test Section";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + sectionName + "\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testSectionController_UpdateSection() throws Exception {
        Long sectionId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.put("/api/sections/{id}", sectionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Section\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testSectionController_DeleteSection() throws Exception {
        Long sectionId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/sections/{id}", sectionId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}