package natlex.example.geologicalproject.service;

import natlex.example.geologicalproject.data.entity.JobResult;

import java.util.concurrent.CompletableFuture;

public interface ExportFileService {
    CompletableFuture<JobResult> exportAsync();
    JobResult getExportStatus(Long id);
}
