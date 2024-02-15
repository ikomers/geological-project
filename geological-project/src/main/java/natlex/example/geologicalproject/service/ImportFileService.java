package natlex.example.geologicalproject.service;

import natlex.example.geologicalproject.data.entity.JobResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface ImportFileService {

    void importData(MultipartFile file, JobResult jobResult);
    CompletableFuture<JobResult> importAsync(MultipartFile file);
    JobResult getImportStatus(Long id);
}
