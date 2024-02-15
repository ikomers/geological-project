package natlex.example.geologicalproject.controller;

import natlex.example.geologicalproject.data.entity.JobResult;
import natlex.example.geologicalproject.service.impl.ImportFileServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.concurrent.CompletableFuture;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class ImportFileControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImportFileServiceImpl importFileService;

    @Test
    public void testImportData() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/import").file(file))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetImportStatus() throws Exception {
        CompletableFuture<JobResult> jobResultFuture = importFileService.importAsync(new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes()));

        mockMvc.perform(MockMvcRequestBuilders.get("/import/{id}", jobResultFuture.join().getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
