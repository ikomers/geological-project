package natlex.example.geologicalproject.service;

import natlex.example.geologicalproject.data.entity.JobResult;
import org.springframework.web.multipart.MultipartFile;

public interface ImportFileService {

    void importData(MultipartFile file, JobResult jobResult);

    JobResult importAsync(MultipartFile file);
}
