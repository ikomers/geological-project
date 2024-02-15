package natlex.example.geologicalproject.service;

import natlex.example.geologicalproject.data.dtos.GeologicalClassDto;
import natlex.example.geologicalproject.data.entity.GeologicalClass;

import java.util.List;

public interface GeologicalClassService {
    List<GeologicalClass> findAll();
    GeologicalClass findById(Long id);
    GeologicalClass save(GeologicalClass geologicalClass);
    void update(Long id, GeologicalClass updatedGeologicalClass);
    void patch(Long id, GeologicalClassDto geologicalClassDto);
    void delete(Long id);
    void saveAll(List<GeologicalClass> geologicalClasses);
}
