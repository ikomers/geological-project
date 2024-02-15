package natlex.example.geologicalproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import natlex.example.geologicalproject.aspect.annotation.Authorized;
import natlex.example.geologicalproject.data.RestResponse;
import natlex.example.geologicalproject.data.entity.JobResult;
import natlex.example.geologicalproject.service.ExportFileService;
import natlex.example.geologicalproject.service.ImportFileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/files")
@Slf4j
@RequiredArgsConstructor
public class FileController {
    private final ImportFileService importFileService;
    private final ExportFileService exportFileService;

    @Authorized
    @PostMapping("/import")
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse importFile(@RequestParam("file") MultipartFile file) {
        log.info("Received request to import a file: {}", file.getOriginalFilename());
        return new RestResponse(importFileService.importAsync(file));
    }

    @Authorized
    @GetMapping("/export")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<JobResult> exportFile() {
        log.info("Received request to export a file.");
        CompletableFuture<JobResult> exportResult = exportFileService.exportAsync();
        return new ResponseEntity<>(exportResult.join(), HttpStatus.OK);
    }
}
