package natlex.example.geologicalproject.service.impl;

import lombok.RequiredArgsConstructor;
import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.data.entity.Section;
import natlex.example.geologicalproject.exceptions.NotFoundException;
import natlex.example.geologicalproject.repositories.GeologicalClassRepository;
import natlex.example.geologicalproject.repositories.SectionRepository;
import natlex.example.geologicalproject.service.SectionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;
    private final GeologicalClassRepository geologicalClassRepository;

    @Override
    public Section findById(Long id) {
        return sectionRepository.findById(id).orElseThrow(() -> new NotFoundException("Section not found"));
    }
    //TODO пробрасывание ошибок
    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    @Override
    public Section createSection(Section section) {
        Section savedSection = sectionRepository.save(section); // save with ID

        for (GeologicalClass geologicalClass : savedSection.getGeologicalClasses()) {
            geologicalClass.setSection(savedSection); // relation
        }

        geologicalClassRepository.saveAll(savedSection.getGeologicalClasses());

        return savedSection;
    }

    @Override
    public Section updateSection(Long sectionId, Section updatedSection) {
        Section existingSection = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("Section not found with id: " + sectionId));

        existingSection.setName(updatedSection.getName());
        existingSection.setGeologicalClasses(updatedSection.getGeologicalClasses());

        List<GeologicalClass> updatedGeologicalClasses = updatedSection.getGeologicalClasses();
        List<GeologicalClass> existingGeologicalClasses = existingSection.getGeologicalClasses();

        int bound = Math.min(updatedGeologicalClasses.size(), existingGeologicalClasses.size());
        IntStream.range(0, bound).forEachOrdered(i -> {
            GeologicalClass updatedGeoClass = updatedGeologicalClasses.get(i);
            GeologicalClass existingGeoClass = existingGeologicalClasses.get(i);

            existingGeoClass.setId(updatedGeoClass.getId());

            existingGeoClass.setName(updatedGeoClass.getName());
            existingGeoClass.setCode(updatedGeoClass.getCode());
        });

        return sectionRepository.save(existingSection);
    }

    @Override
    public void deleteSection(Long sectionId) {
        Section existingSection = findById(sectionId);
        sectionRepository.delete(existingSection);
    }
}
