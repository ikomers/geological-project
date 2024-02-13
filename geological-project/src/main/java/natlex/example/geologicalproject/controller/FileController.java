package natlex.example.geologicalproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import natlex.example.geologicalproject.aspect.annotation.Authorized;
import natlex.example.geologicalproject.data.entity.JobResult;
import natlex.example.geologicalproject.service.impl.ImportExportServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/files")
@Slf4j
@RequiredArgsConstructor
@Authorized
public class FileController {
    private final ImportExportServiceImpl importExportService;

    @PostMapping("/import")
    public ResponseEntity<JobResult> importFile(@RequestParam("file") MultipartFile file) {
        log.info("Received request to import a file: {}", file.getOriginalFilename());
        CompletableFuture<JobResult> importResult = importExportService.importAsync(file);
        return new ResponseEntity<>(importResult.join(), HttpStatus.OK);
    }

    @GetMapping("/export")
    public ResponseEntity<JobResult> exportFile() {
        log.info("Received request to export a file.");
        CompletableFuture<JobResult> exportResult = importExportService.exportAsync();
        return new ResponseEntity<>(exportResult.join(), HttpStatus.OK);
    }
}
