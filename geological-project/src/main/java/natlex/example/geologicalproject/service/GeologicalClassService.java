package natlex.example.geologicalproject.service;

import natlex.example.geologicalproject.data.entity.GeologicalClass;

import java.util.List;

public interface GeologicalClassService {
    List<GeologicalClass> findAll();
    GeologicalClass findById(Long id);
    GeologicalClass save(GeologicalClass geologicalClass);
    void update(Long id, GeologicalClass updatedGeologicalClass);
    void delete(Long id);

    void saveAll(List<GeologicalClass> geologicalClasses);
}
