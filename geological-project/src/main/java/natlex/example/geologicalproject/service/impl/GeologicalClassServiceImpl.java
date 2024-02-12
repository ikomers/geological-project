package natlex.example.geologicalproject.service.impl;

import lombok.RequiredArgsConstructor;
import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.data.entity.Section;
import natlex.example.geologicalproject.exceptions.NotFoundException;
import natlex.example.geologicalproject.repositories.GeologicalClassRepository;
import natlex.example.geologicalproject.service.GeologicalClassService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class GeologicalClassServiceImpl implements GeologicalClassService {
    private final GeologicalClassRepository geologicalClassRepository;

    @Override
    public List<GeologicalClass> findAll() {
        return geologicalClassRepository.findAll();
    }

    @Override
    public GeologicalClass findById(Long id) {
        return geologicalClassRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("GeologicalClass not found with id: " + id));
    }


    @Override
    public GeologicalClass save(GeologicalClass geologicalClass) {
        return geologicalClassRepository.save(geologicalClass);
    }

    @Override
    public GeologicalClass update(Long id, GeologicalClass updatedGeologicalClass) {
        GeologicalClass existingGeologicalClass = findById(id);

        Section existingSection = existingGeologicalClass.getSection();
        updatedGeologicalClass.setSection(existingSection);

        existingGeologicalClass.setName(updatedGeologicalClass.getName());
        existingGeologicalClass.setCode(updatedGeologicalClass.getCode());

        return geologicalClassRepository.save(existingGeologicalClass);
    }

    public void updateAll(Section section, List<GeologicalClass> updatedGeologicalClasses) {
        List<GeologicalClass> existingGeologicalClasses = section.getGeologicalClasses();

        existingGeologicalClasses.removeIf(geoClass -> !updatedGeologicalClasses.contains(geoClass));

        updatedGeologicalClasses.forEach(updatedGeoClass -> {
            if (updatedGeoClass.getId() == null || !existingGeologicalClasses.contains(updatedGeoClass)) {
                updatedGeoClass.setSection(section);
                existingGeologicalClasses.add(updatedGeoClass);
            }
        });

        existingGeologicalClasses.forEach(existingGeoClass -> {
            GeologicalClass updatedGeoClass = updatedGeologicalClasses.stream()
                    .filter(geoClass -> geoClass.getId().equals(existingGeoClass.getId()))
                    .findFirst()
                    .orElse(null);

            if (updatedGeoClass != null) {
                existingGeoClass.setName(updatedGeoClass.getName());
                existingGeoClass.setCode(updatedGeoClass.getCode());
            }
        });
    }

    @Override
    public void delete(Long id) {
        geologicalClassRepository.deleteById(id);
    }
}
