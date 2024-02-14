package natlex.example.geologicalproject.service;


import natlex.example.geologicalproject.data.entity.Section;

import java.util.List;

public interface SectionService {
    Section findById(Long id);

    List<Section> findAll();

    void save(Section section);

    void update(Long sectionId, Section section);

    Section findByName(String name);

    void delete(Long sectionId);

    List<Section> getSectionsByGeologicalClassCode(String code);
}
