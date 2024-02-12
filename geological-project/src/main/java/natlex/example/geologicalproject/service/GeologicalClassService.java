package natlex.example.geologicalproject.service;

import natlex.example.geologicalproject.data.entity.GeologicalClass;
import natlex.example.geologicalproject.data.entity.Section;

import java.util.List;

public interface GeologicalClassService {
    List<GeologicalClass> findAll();
    GeologicalClass findById(Long id);
    GeologicalClass save(GeologicalClass geologicalClass);
    GeologicalClass update(Long id, GeologicalClass updatedGeologicalClass);
    void updateAll(Section section, List<GeologicalClass> updatedGeologicalClasses);
    void delete(Long id);
}
