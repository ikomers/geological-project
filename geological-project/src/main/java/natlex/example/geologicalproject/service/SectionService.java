package natlex.example.geologicalproject.service;


import natlex.example.geologicalproject.data.entity.Section;

import java.util.List;

public interface SectionService {
    Section findById(Long id);

    List<Section> getAllSections();

    Section createSection(Section section);

    Section updateSection(Long sectionId, Section section);

    void deleteSection(Long sectionId);
}
