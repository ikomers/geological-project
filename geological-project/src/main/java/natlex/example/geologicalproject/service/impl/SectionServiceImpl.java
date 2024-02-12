package natlex.example.geologicalproject.service.impl;

import lombok.RequiredArgsConstructor;
import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.data.entity.Section;
import natlex.example.geologicalproject.exceptions.NotFoundException;
import natlex.example.geologicalproject.repositories.GeologicalClassRepository;
import natlex.example.geologicalproject.repositories.SectionRepository;
import natlex.example.geologicalproject.service.SectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;
    private final GeologicalClassServiceImpl geologicalClassService;

    @Override
    public Section findById(Long id) {
        return sectionRepository.findById(id).orElseThrow(() -> new NotFoundException("Section not found"));
    }

    public List<Section> findAll() {
        return sectionRepository.findAll();
    }


    @Override
    public Section findByName(String name) {
        return sectionRepository.findByName(name);
    }

    @Override
    @Transactional
    public Section save(Section section) {
        Section savedSection = sectionRepository.save(section);
        geologicalClassService.updateAll(savedSection, savedSection.getGeologicalClasses());
        return savedSection;
    }

    @Override
    @Transactional
    public Section update(Long sectionId, Section updatedSection) {
         Section existingSection = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("Section not found with id: " + sectionId));

        existingSection.setName(updatedSection.getName());
        geologicalClassService.updateAll(existingSection, updatedSection.getGeologicalClasses());

        return existingSection;
    }

    @Override
    public void delete(Long sectionId) {
        Section existingSection = findById(sectionId);
        sectionRepository.delete(existingSection);
    }
}

      /*existingSection.setGeologicalClasses(updatedSection.getGeologicalClasses());

        List<GeologicalClass> updatedGeologicalClasses = updatedSection.getGeologicalClasses();
        List<GeologicalClass> existingGeologicalClasses = existingSection.getGeologicalClasses();

        int bound = Math.min(updatedGeologicalClasses.size(), existingGeologicalClasses.size());
        IntStream.range(0, bound).forEachOrdered(i -> {
            GeologicalClass updatedGeoClass = updatedGeologicalClasses.get(i);
            GeologicalClass existingGeoClass = existingGeologicalClasses.get(i);

            existingGeoClass.setName(updatedGeoClass.getName());
            existingGeoClass.setCode(updatedGeoClass.getCode());
        });
        return sectionRepository.save(existingSection);*/