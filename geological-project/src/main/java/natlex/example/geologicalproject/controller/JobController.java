package natlex.example.geologicalproject.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import natlex.example.geologicalproject.data.RestResponse;
import natlex.example.geologicalproject.data.entity.JobResult;
import natlex.example.geologicalproject.service.impl.ExportFileServiceImpl;
import natlex.example.geologicalproject.service.impl.ImportFileServiceImpl;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@RequiredArgsConstructor
public class JobController {
    private final ImportFileServiceImpl importFileService;
    private final ExportFileServiceImpl exportFileService;

    @PostMapping("/import")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<RestResponse> importData(@RequestParam MultipartFile file) {
        log.info("Received request to import data from file: {}", file.getOriginalFilename());
        return importFileService.importAsync(file)
                .thenApply(jobResult -> new RestResponse("Import job created with ID: " + jobResult.getId()));
    }

    @GetMapping("/import/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse getImportStatus(@PathVariable Long id) {
        log.info("Received request to get import status by ID: {}", id);
        JobResult jobResult = importFileService.getImportStatus(id);
        return new RestResponse(jobResult.getStatus().name());
    }

    @GetMapping("/export")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<JobResult> exportData() {
        log.info("Received request to export data to file");
        return exportFileService.exportAsync();
    }

    @GetMapping("/export/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse getExportStatus(@PathVariable Long id) {
        log.info("Received request to get export status by ID: {}", id);
        JobResult jobResult = exportFileService.getExportStatus(id);
        return new RestResponse(jobResult.getStatus().name());
    }

    @GetMapping("/export/{id}/file")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        log.info("Received request to download file by export ID: {}", id);
        Resource file = exportFileService.downloadFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}

