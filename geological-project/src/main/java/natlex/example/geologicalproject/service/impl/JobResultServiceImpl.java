package natlex.example.geologicalproject.service.impl;


import lombok.RequiredArgsConstructor;
import natlex.example.geologicalproject.data.entity.JobResult;
import natlex.example.geologicalproject.data.entity.JobStatus;
import natlex.example.geologicalproject.exceptions.NotFoundException;
import natlex.example.geologicalproject.repositories.JobResultRepository;
import natlex.example.geologicalproject.service.JobResultService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class JobResultServiceImpl implements JobResultService {
    private final JobResultRepository jobResultRepository;

    @Override
    @Transactional
    public void save(JobResult jobResult) {
        jobResultRepository.save(jobResult);
    }

    @Override
    public JobResult findById(Long id) {
        return jobResultRepository.findById(id).orElseThrow(() -> new NotFoundException("Job result not found"));
    }

    @Override
    @Transactional
    public JobResult create() {
        JobResult jobResult = new JobResult();
        jobResult.setStatus(JobStatus.IN_PROGRESS);
        save(jobResult);
        return jobResult;
    }
}
