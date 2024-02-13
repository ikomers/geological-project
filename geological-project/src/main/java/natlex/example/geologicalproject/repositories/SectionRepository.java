package natlex.example.geologicalproject.repositories;

import natlex.example.geologicalproject.data.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    Section findByName(String name);
//    List<Section> findByGeologicalClassesCode(String code);
}
