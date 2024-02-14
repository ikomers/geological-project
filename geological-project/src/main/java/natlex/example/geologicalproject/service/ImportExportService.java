package natlex.example.geologicalproject.service;

import natlex.example.geologicalproject.data.entity.JobResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface ImportExportService {
    void importData(MultipartFile file, JobResult jobResult);

    JobResult importAsync(MultipartFile file);

    CompletableFuture<JobResult> exportAsync();
}
