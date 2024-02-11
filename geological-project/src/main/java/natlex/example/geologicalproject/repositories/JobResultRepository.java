package natlex.example.geologicalproject.repositories;



import natlex.example.geologicalproject.data.entity.JobResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobResultRepository extends JpaRepository<JobResult, Long> {

}