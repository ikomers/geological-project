package natlex.example.geologicalproject.service.impl;

import lombok.RequiredArgsConstructor;
import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.data.entity.Section;
import natlex.example.geologicalproject.data.mapper.SectionMapper;
import natlex.example.geologicalproject.exceptions.NotFoundException;
import natlex.example.geologicalproject.repositories.SectionRepository;
import natlex.example.geologicalproject.service.SectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;
    private final GeologicalClassServiceImpl geologicalClassService;

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
    @Transactional
    public void save(Section section) {
        sectionRepository.save(section);

        //copy of list to avoid ConcurrentModificationException
        List<GeologicalClass> geologicalClasses = new ArrayList<>(section.getGeologicalClasses());

        geologicalClasses.forEach(geologicalClassService::save);
    }

    @Override
    @Transactional
    public void update(Long sectionId, Section updatedSection) {
         Section existingSection = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("Section not found with id: " + sectionId));

        existingSection.setName(updatedSection.getName());
        existingSection.getGeologicalClasses().clear();

        geologicalClassService.saveAll(updatedSection.getGeologicalClasses());

        updatedSection.getGeologicalClasses().forEach(geoClass -> geoClass.setSection(existingSection));

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
