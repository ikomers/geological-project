package natlex.example.geologicalproject.service.impl;

import lombok.RequiredArgsConstructor;
import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.data.entity.Section;
import natlex.example.geologicalproject.exceptions.NotFoundException;
import natlex.example.geologicalproject.repositories.SectionRepository;
import natlex.example.geologicalproject.service.GeologicalClassService;
import natlex.example.geologicalproject.service.SectionService;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;
    private final GeologicalClassService geologicalClassService;

    @Override
    public Section findById(Long id) {
        return sectionRepository.findById(id).orElseThrow(() -> new NotFoundException("Section not found"));
    }

    @Override
    public List<Section> findAll() {
        return sectionRepository.findAll();
    }

    //TODO
    @Override
    public List<Section> getSectionsByGeologicalClassCode(String code) {
        return sectionRepository.findByGeologicalClassesCode(code);
    }

    @Override
    @Transactional (propagation = Propagation.REQUIRED)
    public void save(Section section) {
        sectionRepository.save(section);

        //copy of list to avoid ConcurrentModificationException
        List<GeologicalClass> geologicalClasses = new ArrayList<>(section.getGeologicalClasses());

        geologicalClasses.forEach(geologicalClassService::save);
    }

    @Override
    public void update(Long sectionId, Section updatedSection) {
         Section existingSection = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("Section not found with id: " + sectionId));

        if (existingSection != updatedSection) {
            existingSection.setName(updatedSection.getName());
            existingSection.getGeologicalClasses().clear();
        }

        geologicalClassService.saveAll(updatedSection.getGeologicalClasses());

        updatedSection.getGeologicalClasses().forEach(geoClass -> {
            geoClass.setSection(existingSection);
            geologicalClassService.save(geoClass);
        });
    }

    @Override
    public void saveAll(List<Section> sections) {
        // 1. Get all names of sections
        List<String> sectionNames = new ArrayList<>();
        sections.stream().map(Section::getName).forEach(sectionNames::add);

        if (!sectionRepository.findAllByNameIn(sectionNames).isEmpty()) {
            throw new RuntimeException("Sections with these names already exist in the database.");
        }

        // 2. Deep copy of list
        List<Section> sectionsForSaving = sections.stream()
                .map(SerializationUtils::clone)
                .toList();

        // 3. Save all sections without GC for getting sectionId
        sections.forEach(section -> section.setGeologicalClasses(new ArrayList<>()));
        sections.forEach(this::save);

        // 4. Set section id for section and for GC
        for (int i = 0; i < sectionsForSaving.size(); i++) {
            Section savedSection = sectionsForSaving.get(i);
            Section originalSection = sections.get(i);
            Long sectionId = originalSection.getId();
            savedSection.setId(sectionId);

            //Set section id for geological classes
            savedSection.getGeologicalClasses().forEach(geoClass -> geoClass.setSection(savedSection));
        }

        // 5. Save sections
        sectionsForSaving.forEach(this::save);
    }

    @Override
    public void delete(Long sectionId) {
        Section existingSection = findById(sectionId);
        sectionRepository.delete(existingSection);
    }

        @Override
    public Section findByName(String name) {
        return sectionRepository.findByName(name);
    }
}
