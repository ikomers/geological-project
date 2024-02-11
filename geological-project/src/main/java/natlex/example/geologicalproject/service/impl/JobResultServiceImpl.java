package natlex.example.geologicalproject.service.impl;


import lombok.RequiredArgsConstructor;
import natlex.example.geologicalproject.data.entity.JobResult;
import natlex.example.geologicalproject.data.entity.JobStatus;
import natlex.example.geologicalproject.repositories.JobResultRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class JobResultServiceImpl {
    private final JobResultRepository jobResultRepository;

    @Async
    public CompletableFuture<JobResult> importData(String filePath) {
        // Ваш код для обработки файла и сохранения результата
        JobResult result = new JobResult();
        result.setStatus(JobStatus.DONE);
        result.setResult("Import successful");
        result.setTimestamp(LocalDateTime.now());
        // Сохранение в базу данных
        jobResultRepository.save(result);
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<JobResult> exportData(String filePath) {
        // Ваш код для обработки файла и сохранения результата
        JobResult result = new JobResult();
        result.setStatus(JobStatus.DONE);
        result.setResult("Export successful");
        result.setTimestamp(LocalDateTime.now());
        // Сохранение в базу данных
        jobResultRepository.save(result);
        return CompletableFuture.completedFuture(result);
    }

    private JobResult handleError(Exception e) {
        JobResult errorResult = new JobResult();
        errorResult.setStatus(JobStatus.ERROR);
        errorResult.setResult("An error occurred: " + e.getMessage());
        errorResult.setTimestamp(LocalDateTime.now());
        jobResultRepository.save(errorResult);
        return errorResult;
    }
}
