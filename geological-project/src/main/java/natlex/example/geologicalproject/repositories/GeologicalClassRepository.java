package natlex.example.geologicalproject.repositories;

import natlex.example.geologicalproject.data.entity.GeologicalClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeologicalClassRepository extends JpaRepository<GeologicalClass, Long> {

    List<GeologicalClass> findBySectionId(Long sectionId);

}
