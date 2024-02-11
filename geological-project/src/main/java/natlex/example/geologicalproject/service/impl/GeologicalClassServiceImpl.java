package natlex.example.geologicalproject.service.impl;

import lombok.RequiredArgsConstructor;
import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.data.entity.Section;
import natlex.example.geologicalproject.exceptions.NotFoundException;
import natlex.example.geologicalproject.repositories.GeologicalClassRepository;
import natlex.example.geologicalproject.service.GeologicalClassService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeologicalClassServiceImpl implements GeologicalClassService {
    private final GeologicalClassRepository geologicalClassRepository;

    @Override
    public List<GeologicalClass> getAllGeologicalClasses() {
        return geologicalClassRepository.findAll();
    }

    @Override
    public GeologicalClass getGeologicalClassById(Long id) {
        return geologicalClassRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("GeologicalClass not found with id: " + id));
    }


    @Override
    public GeologicalClass createGeologicalClass(GeologicalClass geologicalClass) {
        return geologicalClassRepository.save(geologicalClass);
    }

    @Override
    public GeologicalClass updateGeologicalClass(Long id, GeologicalClass updatedGeologicalClass) {
        GeologicalClass existingGeologicalClass = getGeologicalClassById(id);

        Section existingSection = existingGeologicalClass.getSection();
        updatedGeologicalClass.setSection(existingSection);

        existingGeologicalClass.setName(updatedGeologicalClass.getName());
        existingGeologicalClass.setCode(updatedGeologicalClass.getCode());

        return geologicalClassRepository.save(existingGeologicalClass);
    }

    @Override
    public void deleteGeologicalClass(Long id) {
        geologicalClassRepository.deleteById(id);
    }
}
