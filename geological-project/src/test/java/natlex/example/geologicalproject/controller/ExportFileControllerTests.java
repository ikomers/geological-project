package natlex.example.geologicalproject.controller;

import natlex.example.geologicalproject.data.entity.JobResult;
import natlex.example.geologicalproject.service.impl.ExportFileServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.concurrent.CompletableFuture;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class ExportFileControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExportFileServiceImpl exportFileService;

    @Test
    public void testExportData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/export"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetExportStatus() throws Exception {
        CompletableFuture<JobResult> jobResultFuture = exportFileService.exportAsync();

        mockMvc.perform(MockMvcRequestBuilders.get("/export/{id}", jobResultFuture.join().getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testDownloadFile() throws Exception {
        CompletableFuture<JobResult> jobResultFuture = exportFileService.exportAsync();

        mockMvc.perform(MockMvcRequestBuilders.get("/export/{id}/file", jobResultFuture.join().getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
