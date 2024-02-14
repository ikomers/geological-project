package natlex.example.geologicalproject.service;

import natlex.example.geologicalproject.data.entity.JobResult;

public interface JobResultService {
    void save(JobResult jobResult);
    JobResult findById(Long id);
    JobResult create();
}
