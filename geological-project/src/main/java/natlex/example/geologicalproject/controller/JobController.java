package natlex.example.geologicalproject.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import natlex.example.geologicalproject.aspect.annotation.Authorized;
import natlex.example.geologicalproject.data.entity.JobResult;
import natlex.example.geologicalproject.service.impl.JobResultServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@AllArgsConstructor
public class JobController {
    private final JobResultServiceImpl jobService;

    @Authorized
    @PostMapping("/import")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<JobResult> importData(@RequestParam String filePath) {
        log.info("Received request to import data from file: {}", filePath);
        return jobService.importData(filePath);
    }

    @PostMapping("/export")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CompletableFuture<JobResult> exportData(@RequestParam String filePath) {
        log.info("Received request to export data to file: {}", filePath);
        return jobService.exportData(filePath);
    }
}
