package natlex.example.geologicalproject.service;


import natlex.example.geologicalproject.data.entity.Section;

import java.util.List;

public interface SectionService {
    Section findById(Long id);

    List<Section> findAll();

    Section save(Section section);

    Section update(Long sectionId, Section section);

    Section findByName(String name);

    void delete(Long sectionId);
}
